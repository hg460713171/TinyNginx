package com.h.system.tinynignx.loadbalance;

import java.net.http.HttpRequest;

public interface LoadBalancer {
    BaseRouter getRouter(HttpRequest request, String host);

}
