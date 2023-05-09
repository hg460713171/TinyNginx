package com.h.system.tinynignx.server;

import com.h.system.tinynignx.util.LifeCycle;
import com.h.system.tinynignx.util.ResourcesService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ThreadFactory;

public class NettyServer implements LifeCycle {
    private EventLoopGroup group;  // 通过nio方式来接收连接和处理连接
    private ServerBootstrap bootstrap;


    public static Class<? extends ServerSocketChannel> getSocketChannel(){

        return Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }

    public static EventLoopGroup newEventLoopGroup(int nThread, ThreadFactory threadFactory){

        return Epoll.isAvailable() ? new EpollEventLoopGroup(nThread, threadFactory) : new NioEventLoopGroup(nThread,threadFactory);
    }

    @Override
    public void init() throws Exception {
        try {
            ResourcesService rs = ResourcesService.getInstance();
            String port = rs.getProperties("port");
            ServerBootstrap bootstrap = new ServerBootstrap();
            EventLoopGroup group =  newEventLoopGroup(4,null);
            this.group = group;
            this.bootstrap = bootstrap;
            bootstrap.group(group);
            bootstrap.channel(getSocketChannel());
            bootstrap.childHandler(new NettyServerFilter())
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true);
            // 服务器绑定端口监听
            ChannelFuture f = bootstrap.bind(Integer.parseInt(port)).sync();

            System.out.println("服务端启动成功...");
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully(); ////关闭EventLoopGroup，释放掉所有资源包括创建的线程
        }
    }

    @Override
    public void destroy() throws Exception {
        group.shutdownGracefully();
    }
}