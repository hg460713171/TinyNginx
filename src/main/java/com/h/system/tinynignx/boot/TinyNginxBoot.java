package com.h.system.tinynignx.boot;

import com.h.system.tinynignx.loadbalance.LoadBalancerHolder;
import com.h.system.tinynignx.pool.ChannelPoolHolder;
import com.h.system.tinynignx.server.NettyServer;
import com.h.system.tinynignx.util.ResourcesService;

public class TinyNginxBoot {
    public static void main(String[] args) throws Throwable {
        //单例 先初始化了
        ResourcesService.getInstance();
        //初始化负载均衡器
        LoadBalancerHolder.getInstance().init();

        // 初始化 channelpool  lb组件既是server 用于接收前段的请求，又是client，接收完前端的请求过后，需要对真实的服务器发起请求
        // 这时候，lb 作为client收到消息 ，并把消息返回给前端
        // channelpool 用于 server-client的对应关系
        // 前端--（连接1）-> lb --（连接2）-> 后端 ---（连接2）--> lb --（通过channelpool 找到连接1）--> 前端
        ChannelPoolHolder.getInstance().init();

        //初始化 server（代理静态资源+动态资源） 并wait 主线程
        NettyServer nettyServer = new NettyServer();
        nettyServer.init();


    }
}
