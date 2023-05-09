package com.h.system.tinynignx.boot;

import com.h.system.tinynignx.loadbalance.LoadBalancerHolder;
import com.h.system.tinynignx.server.NettyServer;
import com.h.system.tinynignx.util.ResourcesService;

public class TinyNginxBoot {
    public static void main(String[] args) throws Throwable {
        //单例 先初始化了
        ResourcesService.getInstance();
        LoadBalancerHolder lh = LoadBalancerHolder.getInstance();
        lh.init();

        //初始化 server（代理静态资源+动态资源） 并wait 主线程
        NettyServer nettyServer = new NettyServer();
        nettyServer.init();
        //初始化负载均衡器

    }
}
