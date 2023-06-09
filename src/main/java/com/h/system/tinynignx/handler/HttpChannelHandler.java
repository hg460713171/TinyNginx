package com.h.system.tinynignx.handler;

import com.h.system.tinynignx.client.HttpClient;
import com.h.system.tinynignx.loadbalance.AbstractLoadBalancer;
import com.h.system.tinynignx.loadbalance.BaseRouter;
import com.h.system.tinynignx.loadbalance.LoadBalancerHolder;
import com.h.system.tinynignx.pool.ChannelPool;
import com.h.system.tinynignx.pool.ChannelPoolHolder;
import com.h.system.tinynignx.pool.NettyChannelPool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;


public class HttpChannelHandler implements  MyChannelInboundHandlerAdapter{
    public void channelRead(ChannelHandlerContext ctx, Object msg)  {
        String result ="";
        if(! (msg instanceof FullHttpRequest)){
            result="未知请求!";
            send(ctx,result, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        FullHttpRequest httpRequest = (FullHttpRequest)msg;
        try{
            String path=httpRequest.uri();          //获取路径
            String body = getBody(httpRequest);     //获取参数
            HttpMethod method=httpRequest.method();//获取请求方法
            //如果不是这个路径，就直接返回错误
            System.out.println("body:"+body);

            AbstractLoadBalancer al = LoadBalancerHolder.getInstance().getLoadBalancer();
            InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            BaseRouter baseRouter = al.getRouter(httpRequest,socketAddress.getHostString());

            NettyChannelPool pool = NettyChannelPool.getInstance();

            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, path,
                    Unpooled.wrappedBuffer(body.getBytes("UTF-8")));

            // 构建http请求
            request.headers().set(HttpHeaderNames.HOST, baseRouter.getIp());
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());


            pool.sendRequest(baseRouter,ctx.channel(),request);

            // 发送http请求


//
//            send(ctx,result,HttpResponseStatus.OK);

        }catch(Exception e){
            System.out.println("处理请求失败!");
            e.printStackTrace();
        }finally{
            //释放请求
            //   httpRequest.release();
        }
    }
    /**
     * 获取body参数
     * @param request
     * @return
     */
    private String getBody(FullHttpRequest request){
        ByteBuf buf = request.content();
        return buf.toString(CharsetUtil.UTF_8);
    }

    /**
     * 发送的返回值
     * @param ctx     返回
     * @param context 消息
     * @param status 状态
     */
    private void send(ChannelHandlerContext ctx, String context,HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(context, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content()
                .readableBytes());
        ctx.channel().writeAndFlush(response);

    }
}
