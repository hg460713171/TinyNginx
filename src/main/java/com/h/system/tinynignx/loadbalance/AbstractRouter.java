package com.h.system.tinynignx.loadbalance;

public class AbstractRouter  implements Comparable<AbstractRouter>{

    private String id;
    /**
     * ip 地址
     */
    private final String ip;



    public Integer getPort() {
        return port;
    }

    /**
     *  端口号
     */
    private final Integer port;

    public AbstractRouter(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public int compareTo(AbstractRouter o) {
        return 0;
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
}
