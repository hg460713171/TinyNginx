package com.h.system.tinynignx.client;
 
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
 
import java.net.URI;
 
 
public class HttpClient {
    private static final boolean SSL = false;

    public ChannelFuture channelFuture;
    public void connect(String host, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new HttpClientFilter());
            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();
            channelFuture = f;
        } finally {
            //workerGroup.shutdownGracefully();
        }
 
    }
 
    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        client.connect("192.168.31.141", 8081);
    }
}