package com.h.system.tinynignx.loadbalance;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 基本概念：
 * weight: 配置文件中指定的该后端的权重，这个值是固定不变的。
 * effective_weight: 后端的有效权重，初始值为weight。
 * 在释放后端时，如果发现和后端的通信过程中发生了错误，就减小effective_weight。
 * 此后有新的请求过来时，在选取后端的过程中，再逐步增加effective_weight，最终又恢复到weight。
 * 之所以增加这个字段，是为了当后端发生错误时，降低其权重。
 * current_weight:
 * 后端目前的权重，一开始为0，之后会动态调整。那么是怎么个动态调整呢？
 * 每次选取后端时，会遍历集群中所有后端，对于每个后端，让它的current_weight增加它的effective_weight，
 * 同时累加所有后端的effective_weight，保存为total。
 * 如果该后端的current_weight是最大的，就选定这个后端，然后把它的current_weight减去total。
 * 如果该后端没有被选定，那么current_weight不用减小。
 *
 * 算法逻辑：
 * 1. 对于每个请求，遍历集群中的所有可用后端，对于每个后端peer执行：
 * peer->current_weight += peer->effecitve_weight。
 *  同时累加所有peer的effective_weight，保存为total。
 * 2. 从集群中选出current_weight最大的peer，作为本次选定的后端。
 * 3. 对于本次选定的后端，执行：peer->current_weight -= total。
 *
 */
public class RoundRobinByWeightLoadBalance extends AbstractLoadBalancer{

    //约定的invoker和权重的键值对


    public RoundRobinByWeightLoadBalance(){
        super();
    }

    /**
     * 算法逻辑：
     * 1. 对于每个请求，遍历集群中的所有可用后端，对于每个后端peer执行：
     *  peer->current_weight += peer->effecitve_weight。
     *  同时累加所有peer的effective_weight，保存为total。
     * 2. 从集群中选出current_weight最大的peer，作为本次选定的后端。
     * 3. 对于本次选定的后端，执行：peer->current_weight -= total。
     *
     * @Return ivoker
     */


    private boolean checkNodes(List<? extends BaseRouter> routers){
        return (routers != null && routers.size() > 0);
    }


//    public static void main(String[] args){
//        List<Router> nodes = new ArrayList<>();
//        Router r1 = new Router("12",12,10);
//        Router r2 = new Router("123",12,1);
//        Router r3 = new Router("1234",12,2);
//        nodes.add(r1);
//        nodes.add(r2);
//        nodes.add(r3);
//        Integer times = 20;
//        RoundRobinByWeightLoadBalance roundRobin = new RoundRobinByWeightLoadBalance();
//        for(int i=0; i<times;i++){
//            System.out.println(roundRobin.getRouter(null,null).getIp());
//
//        }
//
//
//    }

    @Override
    public BaseRouter getRouter(HttpRequest request, String host) {
        if (!checkNodes(routers))
            return null;
        else if (routers.size() == 1) {
            if (routers.get(0).isAvalable())
                return routers.get(0);
            else
                return null;
        }
        Integer total = 0;
        Router nodeOfMaxWeight = null;
        for (BaseRouter node : routers) {
            Router realNode = (Router)node;
            total += realNode.getEffectiveWeight();
            realNode.setCurrentWeight(realNode.getCurrentWeight() + realNode.getEffectiveWeight());
            if (nodeOfMaxWeight == null) {
                nodeOfMaxWeight = realNode;
            }else{
                nodeOfMaxWeight = nodeOfMaxWeight.compareTo(realNode) > 0 ? nodeOfMaxWeight : realNode;
            }
        }
        nodeOfMaxWeight.setCurrentWeight(nodeOfMaxWeight.getCurrentWeight()-total);
        return nodeOfMaxWeight;
    }

    @Override
    public void initRoutes(List<BaseRouter> routers) {
        List<Router> list = new ArrayList<>();
        for(BaseRouter baseRouter: routers){
            Router router = new Router(baseRouter.getIp(),baseRouter.getPort(), baseRouter.getValue());
            list.add(router);
        }
        super.routers = list;
    }
}