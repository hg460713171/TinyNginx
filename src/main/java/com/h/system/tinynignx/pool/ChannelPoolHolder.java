package com.h.system.tinynignx.pool;

import com.h.system.tinynignx.loadbalance.LoadBalancerHolder;
import com.h.system.tinynignx.util.LifeCycle;

import java.util.Map;

public class ChannelPoolHolder implements LifeCycle {

    private static volatile ChannelPoolHolder INSTANCE;
    private ChannelPool channelPool;
    public static ChannelPoolHolder getInstance() {
        if (INSTANCE == null) {
            synchronized(ChannelPoolHolder.class){
                if(INSTANCE == null){
                    INSTANCE = new ChannelPoolHolder();
                }
            }
        }
        return INSTANCE;
    }
    public void removeAll(String clientId){
        String serverId = channelPool.getClientToServer().get(clientId);
        channelPool.getClientToServer().remove(clientId);
        channelPool.getServerChannelMap().remove(serverId);
        channelPool.getClientChannelMap().remove(clientId);
    }
    public ChannelPool getChannelPool() {
        return channelPool;
    }

    @Override
    public void init() throws Exception {
        channelPool = new ChannelPool();
    }

    @Override
    public void destroy() throws Exception {

    }
}
