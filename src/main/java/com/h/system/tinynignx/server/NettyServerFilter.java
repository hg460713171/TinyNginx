package com.h.system.tinynignx.server;

import com.h.system.tinynignx.handle.FileServerHandler;
import com.h.system.tinynignx.handle.NettyHttpHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;


public class NettyServerFilter extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast("encoder",new HttpResponseEncoder());
        channelPipeline.addLast("decoder",new HttpRequestDecoder());
        channelPipeline.addLast("aggregator", new HttpObjectAggregator(10*1024*1024));//把单个http请求转为FullHttpReuest或FullHttpResponse
        channelPipeline.addLast("handler", new NettyHttpHandler());// 服务端业务逻辑
//        channelPipeline.addLast(new ChunkedWriteHandler());
//        channelPipeline.addLast("handler",new FileServerHandler());

    }
}