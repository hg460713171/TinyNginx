package com.h.system.tinynignx.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;

public class HttpClientFilter extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new HttpClientCodec());
        ch.pipeline().addLast("encoder",new HttpResponseEncoder());
        ch.pipeline().addLast("decoder",new HttpRequestDecoder());
        ch.pipeline().addLast("aggregator", new HttpObjectAggregator(10*1024*1024));
        ch.pipeline().addLast("decompressor",new HttpContentDecompressor());
        ch.pipeline().addLast("httphandler",new HttpClientInboundHandler());
    }
}
