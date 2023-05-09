package com.h.system.tinynignx.client;
 
import com.h.system.tinynignx.pool.ChannelPool;
import com.h.system.tinynignx.pool.ChannelPoolHolder;
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
        ChannelPool channelPool = ChannelPoolHolder.getInstance().getChannelPool();
        // 找到对应的serverId 通过id 找到channel
        String serverId = channelPool.getClientToServer().get(ctx.channel().id().asLongText());
        Channel serverChannel = channelPool.getServerChannelMap().get(serverId);
        serverChannel.writeAndFlush(httpResponse);
        ByteBuf content = httpResponse.content();
        System.out.println(content.toString(CharsetUtil.UTF_8));
//        content.release();
        synchronized (this){
            ChannelPoolHolder.getInstance().removeAll(ctx.channel().id().asLongText());
        }
    }
}