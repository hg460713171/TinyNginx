package com.h.system.tinynignx.server;

import com.h.system.tinynignx.handler.FileChannelHandler;
import com.h.system.tinynignx.handler.HttpChannelHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;


public class NettyHttpHandler extends ChannelInboundHandlerAdapter {

    FileChannelHandler fileChannelHandler;
    HttpChannelHandler httpChannelHandler;
    public NettyHttpHandler(){
        fileChannelHandler = new FileChannelHandler();
        httpChannelHandler = new HttpChannelHandler();
    }


    /*
     * 收到消息时，返回信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpRequest httpRequest = (FullHttpRequest)msg;
        String path=httpRequest.uri();          //获取路径

        if(path.startsWith("/webapp/")){
            httpChannelHandler.channelRead(ctx,msg);
        }else{
            fileChannelHandler.channelRead(ctx,msg);
        }
    }


    /*
     * 建立连接时，返回消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String ip = ctx.channel().remoteAddress().toString();
        System.out.println("连接的客户端地址:" +ip);


        System.out.println("dd1:" + ctx.channel().remoteAddress());

        ctx.writeAndFlush("客户端"+ InetAddress.getLocalHost().getHostName() + "成功与服务端建立连接！ ");
        super.channelActive(ctx);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}