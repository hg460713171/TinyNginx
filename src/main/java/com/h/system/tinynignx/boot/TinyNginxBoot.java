package com.h.system.tinynignx.boot;

import com.h.system.tinynignx.util.ResourcesService;

public class TinyNginxBoot {
    public static void main(String[] args) {
        //解析配置文件工具类初始化(单例)
        ResourcesService rs = ResourcesService.getInstance();
        String port = rs.getProperties("port");



    }
}
