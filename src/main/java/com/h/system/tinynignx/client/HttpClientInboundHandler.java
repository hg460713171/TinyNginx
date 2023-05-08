package com.h.system.tinynignx.client;
 
import com.h.system.tinynignx.pool.ChannelPool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;


public class HttpClientInboundHandler
        extends ChannelInboundHandlerAdapter {
 
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //开始对服务器的响应做处理
        FullHttpResponse httpResponse = (FullHttpResponse)msg;
        String serverId = ChannelPool.clientToServer.get(ctx.channel().id().asLongText());
        Channel serverChannel = ChannelPool.ServerChannelMap.get(serverId);
        serverChannel.writeAndFlush(httpResponse);
//        System.out.println(httpResponse.headers());
        ByteBuf content = httpResponse.content();
        System.out.println(content.toString(CharsetUtil.UTF_8));
//        content.release();
 
    }
}