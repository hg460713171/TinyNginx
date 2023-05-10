/*
 * Copyright 2014 The LightNettyClient Project
 *
 * The Light netty client Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.h.system.tinynignx.pool;


import com.h.system.tinynignx.client.HttpClientFilter;
import com.h.system.tinynignx.util.HttpChannelUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class NettyChannelPool {
    private static final Logger  logger = Logger.getLogger(NettyChannelPool.class.getName());

    // channel pools per route
    private ConcurrentMap<String, LinkedBlockingQueue<Channel>> routeToPoolChannels;

    // max number of channels allow to be created per route
    private ConcurrentMap<String, Semaphore>                    maxPerRoute;

    // max time wait for a channel return from pool
    private int                                                 connectTimeOutInMilliSecondes;

    // max idle time for a channel before close
    private int                                                 maxIdleTimeInMilliSecondes;


    /**
     * value is false indicates that when there is not any channel in pool and no new
     * channel allowed to be create based on maxPerRoute, a new channel will be forced
     * to create.Otherwise, a <code>TimeoutException</code> will be thrown
     * */

    // default max number of channels allow to be created per route
    private final static int                                    DEFAULT_MAX_PER_ROUTE = 200;

    private EventLoopGroup                                      group;

    private final Bootstrap                                     clientBootstrap;

    private static final String                                 COLON                 = ":";

    /**
     * Create a new instance of ChannelPool
     *
     * @param maxPerRoute
     *            max number of channels per route allowed in pool
     * @param connectTimeOutInMilliSecondes
     *            max time wait for a channel return from pool
     * @param maxIdleTimeInMilliSecondes
     *            max idle time for a channel before close
     * @param options
     *            user-defined options
     * @param customGroup user defined {@link EventLoopGroup}
     */
    @SuppressWarnings("unchecked")
    public NettyChannelPool(Map<String, Integer> maxPerRoute, int connectTimeOutInMilliSecondes,
                            int maxIdleTimeInMilliSecondes,
                            Map<ChannelOption, Object> options, EventLoopGroup customGroup) {

        this.maxIdleTimeInMilliSecondes = maxIdleTimeInMilliSecondes;
        this.connectTimeOutInMilliSecondes = connectTimeOutInMilliSecondes;
        this.maxPerRoute = new ConcurrentHashMap<String, Semaphore>();
        this.routeToPoolChannels = new ConcurrentHashMap<String, LinkedBlockingQueue<Channel>>();
        this.group = null == customGroup ? new NioEventLoopGroup() : customGroup;

        this.clientBootstrap = new Bootstrap();
        clientBootstrap.group(group).channel(NioSocketChannel.class).option(
            ChannelOption.SO_KEEPALIVE, true).handler(new HttpClientFilter());
        if (null != options) {
            for (Entry<ChannelOption, Object> entry : options.entrySet()) {
                clientBootstrap.option(entry.getKey(), entry.getValue());
            }
        }
        if (null != maxPerRoute) {
            for (Entry<String, Integer> entry : maxPerRoute.entrySet()) {
                this.maxPerRoute.put(entry.getKey(), new Semaphore(entry.getValue()));
            }
        }

    }

    /**
     * send http request to server specified by the route. The channel used to
     * send the request is obtained according to the follow rules
     * <p>
     * 1. poll the first valid channel from pool without waiting. If no valid
     * channel exists, then go to step 2.
     * 2. create a new channel and return. If failed to create a new channel, then go to step 3.
     * Note: the new channel created in this step will be returned to the pool
     * 3. poll the first valid channel from pool within specified waiting time. If no valid
     * channel exists then throw <code>TimeoutException</code>.
     * Otherwise,go to step 4.
     * 4. create a new channel and return. Note: the new channel created in this step will not be returned to the pool.
     * </p>
     *
     * @param route
     *            target server
     * @param request
     *            {@link HttpRequest}
     * @return
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws IOException
     * @throws Exception
     */
    public void sendRequest(InetSocketAddress route, final HttpRequest request)
                                                                                                  throws InterruptedException,
                                                                                                  IOException {
        if (sendRequestUsePooledChannel(route, request, false)) {
            return ;
        }

        if (sendRequestUseNewChannel(route, request)) {
            return;
        }

        if (sendRequestUsePooledChannel(route, request, true)) {
            return;
        }

        throw new IOException("send request failed");
    }

    /**
     * return the specified channel to pool
     *
     * @param channel
     */
    public void returnChannel(Channel channel) {

        InetSocketAddress route = (InetSocketAddress) channel.remoteAddress();
        String key = getKey(route);
        LinkedBlockingQueue<Channel> poolChannels = routeToPoolChannels.get(key);

        if (null != channel && channel.isActive()) {
            if (poolChannels.offer(channel)) {
                logger.log(Level.INFO, channel + "returned");
            }
        }
    }

    /**
     * close all channels in the pool and shut down the eventLoopGroup
     *
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

        for (LinkedBlockingQueue<Channel> queue : routeToPoolChannels.values()) {
            for (Channel channel : queue) {
                removeChannel(channel, null);
                channelGroup.add(channel);
            }
        }
        channelGroup.close().sync();
        group.shutdownGracefully();
    }

    /**
     * remove the specified channel from the pool,cancel the responseFuture
     * and release semaphore for the route
     *
     * @param channel
     */
    private void removeChannel(Channel channel, Throwable cause) {

        InetSocketAddress route = (InetSocketAddress) channel.remoteAddress();
        String key = getKey(route);


        LinkedBlockingQueue<Channel> poolChannels = routeToPoolChannels.get(key);
        if (poolChannels.remove(channel)) {
            logger.log(Level.INFO, channel + " removed");
        }
        getAllowCreatePerRoute(key).release();

    }

    private boolean sendRequestUsePooledChannel(InetSocketAddress route, final HttpRequest request,
                                                boolean isWaiting) throws InterruptedException {
        LinkedBlockingQueue<Channel> poolChannels = getPoolChannels(getKey(route));
        Channel channel = poolChannels.poll();

        while (null != channel && !channel.isActive()) {
            channel = poolChannels.poll();
        }

        if (null == channel || !channel.isActive()) {
            if (!isWaiting) {
                return false;
            }
            channel = poolChannels.poll(connectTimeOutInMilliSecondes, TimeUnit.MILLISECONDS);
            if (null == channel || !channel.isActive()) {
                logger.log(Level.WARNING, "obtain channel from pool timeout");
                return false;
            }
        }

        logger.log(Level.INFO, channel + " reuse");

        channel.writeAndFlush(request).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        return true;
    }

    private boolean sendRequestUseNewChannel(final InetSocketAddress route,
                                             final HttpRequest request) {
        ChannelFuture future = createChannelFuture(route);
        if (null != future) {
            HttpChannelUtils.attributeRoute(future.channel(), route);
            future.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {

                        future.channel().closeFuture().addListener(new ChannelFutureListener() {

                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {

                                logger.log(Level.SEVERE, future.channel() + " closed, exception: "
                                                         + future.cause());
                                removeChannel(future.channel(), future.cause());
                            }

                        });
                        future.channel().writeAndFlush(request).addListener(CLOSE_ON_FAILURE);
                    } else {
                        logger.log(Level.SEVERE, future.channel() + " connect failed, exception: "
                                                 + future.cause());

                        releaseCreatePerRoute(future.channel());
                    }
                }

            });
            return true;
        }
        return false;
    }

    public void releaseCreatePerRoute(Channel channel) {
        InetSocketAddress route = HttpChannelUtils.getRoute(channel);
        getAllowCreatePerRoute(getKey(route)).release();
    }

    private Semaphore getAllowCreatePerRoute(String key) {
        Semaphore allowCreate = maxPerRoute.get(key);
        if (null == allowCreate) {
            Semaphore newAllowCreate = new Semaphore(DEFAULT_MAX_PER_ROUTE);
            allowCreate = maxPerRoute.putIfAbsent(key, newAllowCreate);
            if (null == allowCreate) {
                allowCreate = newAllowCreate;
            }
        }
        return allowCreate;
    }

    private LinkedBlockingQueue<Channel> getPoolChannels(String route) {
        LinkedBlockingQueue<Channel> oldPoolChannels = routeToPoolChannels.get(route);
        if (null == oldPoolChannels) {
            LinkedBlockingQueue<Channel> newPoolChannels = new LinkedBlockingQueue<Channel>();
            oldPoolChannels = routeToPoolChannels.putIfAbsent(route, newPoolChannels);
            if (null == oldPoolChannels) {
                oldPoolChannels = newPoolChannels;
            }
        }
        return oldPoolChannels;
    }

    private String getKey(InetSocketAddress route) {
        return route.getHostName() + COLON + route.getPort();
    }

    private ChannelFuture createChannelFuture(InetSocketAddress route) {
        String key = getKey(route);

        Semaphore allowCreate = getAllowCreatePerRoute(key);
        if (allowCreate.tryAcquire()) {
            try {
                ChannelFuture connectFuture = clientBootstrap.connect(route.getHostName(), route
                    .getPort());
                return connectFuture;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "connect failed", e);
                allowCreate.release();
            }
        }
        return null;
    }
}
