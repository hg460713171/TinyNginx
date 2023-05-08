package com.h.system.tinynignx.pool;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ChannelPool {
    public static Map<String, Channel> clientChannelMap = new ConcurrentHashMap<>();
    public static Map<String, Channel> ServerChannelMap = new ConcurrentHashMap<>();

    public static Map<String, String> clientToServer = new ConcurrentHashMap<>();


}
