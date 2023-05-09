package com.h.system.tinynignx.loadbalance;

import java.net.http.HttpRequest;
import java.util.List;

public interface LoadBalancer {
    AbstractRouter getRouter(HttpRequest request, String host);

}
