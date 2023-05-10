package com.h.system.tinynignx.client;
 
import com.h.system.tinynignx.pool.ChannelPool;
import com.h.system.tinynignx.pool.ChannelPoolHolder;
import com.h.system.tinynignx.pool.NettyChannelPool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.util.logging.Level;
import java.util.logging.Logger;


public class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = Logger.getLogger(HttpClientInboundHandler.class.getName());

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //开始对服务器的响应做处理
        FullHttpResponse httpResponse = (FullHttpResponse)msg;
        ChannelPool channelPool = ChannelPoolHolder.getInstance().getChannelPool();
        // 找到对应的serverId 通过id 找到channel
        String serverId = channelPool.getClientToServer().get(ctx.channel().id().asLongText());
        Channel serverChannel = channelPool.getServerChannelMap().get(serverId);
        serverChannel.writeAndFlush(httpResponse);
        ByteBuf content = httpResponse.content();
        System.out.println(content.toString(CharsetUtil.UTF_8));
//        content.release();
        NettyChannelPool.getInstance().returnChannel(ctx.channel());
        synchronized (this){
            ChannelPoolHolder.getInstance().removeAll(ctx.channel().id().asLongText());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            logger.log(Level.WARNING, "remove idle channel: " + ctx.channel());
            ctx.channel().close();
        } else {
            ctx.fireUserEventTriggered(evt);
        }
    }

}