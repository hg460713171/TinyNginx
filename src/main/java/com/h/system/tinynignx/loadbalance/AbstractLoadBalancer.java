package com.h.system.tinynignx.loadbalance;

import io.netty.handler.codec.http.FullHttpRequest;

import java.net.http.HttpRequest;
import java.util.List;

public abstract class AbstractLoadBalancer implements LoadBalancer{

    List<? extends BaseRouter> routers;



    public abstract BaseRouter getRouter(FullHttpRequest request, String host);


    public void initRoutes(List<BaseRouter> routers){
        this.routers = routers;
    }
}
