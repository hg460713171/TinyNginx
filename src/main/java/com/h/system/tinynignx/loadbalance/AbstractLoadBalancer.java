package com.h.system.tinynignx.loadbalance;

import java.net.http.HttpRequest;
import java.util.List;

public class AbstractLoadBalancer implements LoadBalancer{

    List<? extends AbstractRouter> routers;


    @Override
    public AbstractRouter getRouter(HttpRequest request, String host) {
        return null;
    }
}
