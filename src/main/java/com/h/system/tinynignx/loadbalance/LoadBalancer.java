package com.h.system.tinynignx.loadbalance;

import io.netty.handler.codec.http.FullHttpRequest;


public interface LoadBalancer {
    BaseRouter getRouter(FullHttpRequest request, String host);

}
