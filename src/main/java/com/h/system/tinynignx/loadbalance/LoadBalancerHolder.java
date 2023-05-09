package com.h.system.tinynignx.loadbalance;

import com.h.system.tinynignx.util.LifeCycle;
import com.h.system.tinynignx.util.ResourcesService;

import java.util.ArrayList;
import java.util.List;

public class LoadBalancerHolder implements LifeCycle {
    private static volatile LoadBalancerHolder INSTANCE;
    private AbstractLoadBalancer abstractLoadBalancer;

    public  AbstractLoadBalancer getLoadBalancer(){
        return abstractLoadBalancer;
    }

    public static LoadBalancerHolder getInstance() {
        if (INSTANCE == null) {
            synchronized(LoadBalancerHolder.class){
                if(INSTANCE == null){
                    INSTANCE = new LoadBalancerHolder();
                }
            }
        }
        return INSTANCE;
    }
    @Override
    public void init() throws Exception {
        ResourcesService rc = ResourcesService.getInstance();
        String ips = rc.getProperties("backend.ip");
        String values = rc.getProperties("backend.value");
        // 负载均衡的后端ip
        String[] ipTables = ips.split(",");

        // 负载均衡的后端ip 所对应的参数 ,可以用来扩展 自实现负载均衡算法
        String[] valueTables = values.split(",");
        List<BaseRouter> baseRouterList = new ArrayList<>();

        //通过配置实现 laadbalancer策略 默认为 roundrobin
        String loadBalancer = rc.getProperties("backend.loadbalancer");
        Class<?> loadBalancerClass = Class.forName(loadBalancer);
        AbstractLoadBalancer loadBalancerInstance = (AbstractLoadBalancer) loadBalancerClass.newInstance();
        for(int i=0; i< ipTables.length;i++){
            String[] ipPort = ipTables[i].split(":");
            BaseRouter baseRouter = new BaseRouter(ipPort[0],Integer.valueOf(ipPort[1]),valueTables[i]);
            baseRouterList.add(baseRouter);
        }
        loadBalancerInstance.initRoutes(baseRouterList);
        this.abstractLoadBalancer = loadBalancerInstance;
    }

    @Override
    public void destroy() throws Exception {

    }



}
