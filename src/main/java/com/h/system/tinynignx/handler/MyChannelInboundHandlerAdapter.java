package com.h.system.tinynignx.handler;

import io.netty.channel.ChannelHandlerContext;

public interface MyChannelInboundHandlerAdapter {
    public void channelRead(ChannelHandlerContext ctx, Object msg);
}
