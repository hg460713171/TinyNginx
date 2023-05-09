package com.h.system.tinynignx.loadbalance;

import java.net.http.HttpRequest;
import java.util.List;

public abstract class AbstractLoadBalancer implements LoadBalancer{

    List<? extends BaseRouter> routers;


    @Override
    public abstract BaseRouter getRouter(HttpRequest request, String host);


    public void initRoutes(List<BaseRouter> routers){
        this.routers = routers;
    }
}
