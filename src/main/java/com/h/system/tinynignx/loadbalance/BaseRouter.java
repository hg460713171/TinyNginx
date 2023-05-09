package com.h.system.tinynignx.loadbalance;

public class BaseRouter implements Comparable<BaseRouter>{

    private String id;
    /**
     * ip 地址
     */
    private final String ip;


    private  String value;


    private boolean isAvalable;

    /**
     *  端口号
     */
    private final Integer port;

    public BaseRouter(String ip, Integer port, String value) {
        this.ip = ip;
        this.port = port;
        this.value = value;
        this.isAvalable = true;
    }

    @Override
    public int compareTo(BaseRouter o) {
        return 0;
    }

    public Integer getPort() {
        return port;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public boolean isAvalable() {
        return isAvalable;
    }

    public void setAvalable(boolean avalable) {
        isAvalable = avalable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
