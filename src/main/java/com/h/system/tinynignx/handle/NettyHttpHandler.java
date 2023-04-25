package com.h.system.tinynignx.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;


public class NettyHttpHandler extends ChannelInboundHandlerAdapter {

    private String result="";
    /*
     * 收到消息时，返回信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(! (msg instanceof FullHttpRequest)){
            result="未知请求!";
            send(ctx,result,HttpResponseStatus.BAD_REQUEST);
            return;
        }
        boolean keepAlive = true;
        FullHttpRequest httpRequest = (FullHttpRequest)msg;
        try{
            String path=httpRequest.uri();          //获取路径
            String body = getBody(httpRequest);     //获取参数
            HttpMethod method=httpRequest.method();//获取请求方法
            //如果不是这个路径，就直接返回错误
            if(!"/test".equalsIgnoreCase(path)){
                result="非法请求!";
                send(ctx,result,HttpResponseStatus.BAD_REQUEST);
                return;
            }
            System.out.println("接收到:"+method+" 请求");
            //如果是GET请求
            if(HttpMethod.GET.equals(method)){
                //接受到的消息，做业务逻辑处理...
                System.out.println("body:"+body);
                result="GET请求";
                send(ctx,result,HttpResponseStatus.OK);
                return;
            }
            //如果是POST请求
            if(HttpMethod.POST.equals(method)){
                //接受到的消息，做业务逻辑处理...
                System.out.println("body:"+body);
                result="POST请求";
                new Thread(){
                    @Override
                    public void run() {
                        System.out.println("inAsyn");
                        send(ctx,result,HttpResponseStatus.OK);
                    }
                }.start();
              //  send(ctx,result,HttpResponseStatus.OK);
                return;
            }

            //如果是PUT请求
            if(HttpMethod.PUT.equals(method)){
                //接受到的消息，做业务逻辑处理...
                System.out.println("body:"+body);
                result="PUT请求";
//                new Thread(){
//                    @Override
//                    public void run() {
//                        System.out.println("inAsyn");
//                    }
//                }.start();
                send(ctx,result,HttpResponseStatus.OK);

                return;
            }
            //如果是DELETE请求
            if(HttpMethod.DELETE.equals(method)){
                //接受到的消息，做业务逻辑处理...
                System.out.println("body:"+body);
                result="DELETE请求";
                send(ctx,result,HttpResponseStatus.OK);
                return;
            }
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
        ctx.writeAndFlush(response);

    }

    /*
     * 建立连接时，返回消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
        ctx.writeAndFlush("客户端"+ InetAddress.getLocalHost().getHostName() + "成功与服务端建立连接！ ");
        super.channelActive(ctx);
    }

}