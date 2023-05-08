package com.h.system.tinynignx.handler;

import com.h.system.tinynignx.util.HttpWirteResponseUtil;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class FileChannelHandler {

    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {

        //request.retain();
        HttpResponse response = null;

        FullHttpRequest request = (FullHttpRequest)msg;
        String p=request.uri();          //获取路径


        if("/webapp".equalsIgnoreCase(p)){
            return;
        }
        RandomAccessFile randomAccessFile = null;
        try{
            // 状态为1xx的话，继续请求
            if (HttpHeaders.is100ContinueExpected(request)) {
                send100Continue(channelHandlerContext);
            }
            String uri = request.uri();
//            if(!uri.endsWith(".js") && !uri.endsWith(".css") && !uri.endsWith(".html")){
//                channelHandlerContext.fireChannelRead(request);
//                return;
//            }
            int index = uri.lastIndexOf("/") + 1;
            if(index == -1){
                HttpWirteResponseUtil.writeResponse(request, OK, channelHandlerContext);
                return;
            }
            String filename = uri.substring(index);
            uri = uri.substring(0, index-1);
            String path = "/home/hou/IdeaProjects/TinyNginx/src/main/resources/dist"+uri.toString();

            String fullPath = path+ "/"+filename;
            System.out.println(filename);
            File file = new File(fullPath);
            try {
                randomAccessFile = new RandomAccessFile(file, "r");
            } catch (FileNotFoundException e) {
                HttpWirteResponseUtil.writeResponse(request, NOT_FOUND, channelHandlerContext);
                e.printStackTrace();
                return;
            }

            if(!file.exists() || file.isHidden()){
                HttpWirteResponseUtil.writeResponse(request, NOT_FOUND, channelHandlerContext);
                return;
            }
            long fileLength = randomAccessFile.length();
            response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);

            setContentType(response, file);
            boolean keepAlive =  HttpUtil.isKeepAlive(request);
            if (keepAlive) {
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, fileLength);
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            channelHandlerContext.channel().write(response);


            ChannelFuture sendFileFuture = channelHandlerContext.channel().write(new ChunkedNioFile(randomAccessFile.getChannel()), channelHandlerContext.newProgressivePromise());
            // 写入文件尾部
            sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
                @Override
                public void operationProgressed(ChannelProgressiveFuture future,
                                                long progress, long total) {
//                    if (total < 0) { // total unknown
//                        System.out.println("Transfer progress: " + progress);
//                    } else {
//                        System.out.println("Transfer progress: " + progress + " / "
//                                + total);
//                    }
                }

                @Override
                public void operationComplete(ChannelProgressiveFuture future)
                        throws Exception {
                    System.out.println("Transfer complete.");
                }


            });
            ChannelFuture lastContentFuture =  channelHandlerContext.channel().writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                lastContentFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }finally {
            /*if(randomAccessFile != null){
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
        }
    }

    private void setContentType(HttpResponse response, File file){
        //MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        if(file.getName().endsWith(".js")){
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/x-javascript");
        }else if(file.getName().endsWith(".css")){
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/css; charset=UTF-8");
        }else if (file.getName().endsWith(".html")){
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        }
/*        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        response.headers().set(CONTENT_TYPE,
                mimeTypesMap.getContentType(file.getPath()));*/
    }
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }
}
