package com.h.system.tinynignx.loadbalance;

import com.h.system.tinynignx.util.LifeCycle;
import com.h.system.tinynignx.util.ResourcesService;

import java.util.ArrayList;
import java.util.List;

public class LoadBalancerFactory implements LifeCycle {
    static AbstractLoadBalancer abstractLoadBalancer;

    public static AbstractLoadBalancer getLoadBalancer(){
        return abstractLoadBalancer;
    }

    @Override
    public void init() throws Throwable {
        ResourcesService rc = ResourcesService.getInstance();
        String ips = rc.getProperties("backend.ip");
        String values = rc.getProperties("backend.value");
        String[] ipTables = ips.split(",");
        String[] valueTables = values.split(",");
        List<BaseRouter> baseRouterList = new ArrayList<>();
        String loadBalancer = rc.getProperties("backend.loadbalancer");
        for(int i=0; i< ipTables.length;i++){
            String[] ipPort = ipTables[i].split(":");
            BaseRouter baseRouter = new BaseRouter(ipPort[0],Integer.valueOf(ipPort[1]),valueTables[i]);
            baseRouterList.add(baseRouter);
        }
    }

    @Override
    public void destroy() throws Throwable {

    }


}
