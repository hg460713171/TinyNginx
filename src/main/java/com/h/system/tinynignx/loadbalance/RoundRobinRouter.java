package com.h.system.tinynignx.loadbalance;

public class RoundRobinRouter extends BaseRouter {

    /**
     *  权重 是固定不变的
     */
    private  Integer weight;
    /**
     *
     *  后端的有效权重，初始值为weight。
     *   在释放后端时，如果发现和后端的通信过程中发生了错误，就减小effective_weight。
     *   此后有新的请求过来时，在选取后端的过程中，再逐步增加effective_weight，最终又恢复到weight。
     *  *之所以增加这个字段，是为了当后端发生错误时，降低其权重。
     */
    private Integer effectiveWeight;
    /**
     * 后端目前的权重，一开始为0，之后会动态调整。那么是怎么个动态调整呢？
     *  * 每次选取后端时，会遍历集群中所有后端，对于每个后端，让它的current_weight增加它的effective_weight，
     *  * 同时累加所有后端的effective_weight，保存为total。
     *  * 如果该后端的current_weight是最大的，就选定这个后端，然后把它的current_weight减去total。
     *  * 如果该后端没有被选定，那么current_weight不用减小。
     */
    private Integer currentWeight;

    /**
     * 后端是否可达
     */



    public RoundRobinRouter(String ip, Integer port, String weight) {
        super(ip, port,weight);
        this.weight = Integer.valueOf(weight);
        this.effectiveWeight = Integer.valueOf(weight);
        this.currentWeight = 0;
    }

    public int compareTo(RoundRobinRouter o) {
        return currentWeight > o.currentWeight ? 1 : (currentWeight.equals(o.currentWeight) ? 0 : -1);
    }

    public void onInvokeSuccess(){
        if (effectiveWeight < this.weight)
            effectiveWeight++;
    }

    public void onInvokeFail(){
        effectiveWeight--;
    }
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }




    public Integer getEffectiveWeight() {
        return effectiveWeight;
    }

    public void setEffectiveWeight(Integer effectiveWeight) {
        this.effectiveWeight = effectiveWeight;
    }

    public Integer getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(Integer currentWeight) {
        this.currentWeight = currentWeight;
    }


}
