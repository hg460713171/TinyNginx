package com.h.system.tinynignx.fileserver;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;


public class FileServerFilter extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("encoder",new HttpResponseEncoder());
        ch.pipeline().addLast("decoder",new HttpRequestDecoder());
        ch.pipeline().addLast("aggregator", new HttpObjectAggregator(10*1024*1024));//把单个http请求转为FullHttpReuest或FullHttpResponse
        ch.pipeline().addLast(new ChunkedWriteHandler());
        ch.pipeline().addLast("handler", new FileServerHandler());// 服务端业务逻辑

    }
}