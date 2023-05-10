package com.h.system.tinynignx.pool;

import com.h.system.tinynignx.util.LifeCycle;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ChannelPool  {

    public  Map<String, Channel> clientChannelMap;
    public  Map<String, Channel> ServerChannelMap;

    public  Map<String, String> clientToServer;


    public ChannelPool(){
        clientChannelMap = new ConcurrentHashMap<>();
        ServerChannelMap = new ConcurrentHashMap<>();
        clientToServer = new ConcurrentHashMap<>();
    }

    public Map<String, Channel> getClientChannelMap() {
        return clientChannelMap;
    }


    public Map<String, Channel> getServerChannelMap() {
        return ServerChannelMap;
    }


    public Map<String, String> getClientToServer() {
        return clientToServer;
    }




}
