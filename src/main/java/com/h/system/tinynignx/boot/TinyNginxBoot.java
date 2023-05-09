package com.h.system.tinynignx.boot;

import com.h.system.tinynignx.server.NettyServer;
import com.h.system.tinynignx.util.ResourcesService;

public class TinyNginxBoot {
    public static void main(String[] args) throws Throwable {
        //单例 先初始化了
        ResourcesService.getInstance();
        NettyServer nettyServer = new NettyServer();
        nettyServer.init();
    }
}
