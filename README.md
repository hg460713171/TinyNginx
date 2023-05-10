# TinyNginx
## 概览
此项目为基于netty的L7负载均衡器(http1.1), jdk版本为11


pom如下：

```
        <dependency>
            <groupId>io.netty</groupId>
            <artifactId>netty-all</artifactId>
            <version>4.1.32.Final</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-1.2-api</artifactId>
            <version>2.11.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>2.11.0</version>
        </dependency>
```

首先先放一下线程模型
![1.png](static%2F1.png)


其中 webserver和 webclient 都采用的reactor线程模型


![2.png](static%2F2.png)

## 使用说明
1. 下载
```agsl
git clone https://github.com/hg460713171/TinyNginx.git
```
2. run

main 函数为 TinyNginxBoot.main();

3. 配置前端

resources/dist 里面是一个前端项目 可以替换成你自己的前端。

resources/dist 里的是我从 https://github.com/lin-xin/vue-manage-system.git 下载的。

config.properties的 frontend.url 修改为你自己的前端目录。

3. 启动后端
可以使用自己的后端服务器，也可以使用配套提供的简易后端
```
git clone https://github.com/hg460713171/TinyNginxBackend.git
```

4.参数配置 
参数在 config.properties 里面

```agsl
port=8081  
# tinynginx启动的端口号

backend.prefix = webapp   
# 以/webapp为前缀的将会进行反向代理，否则访问本地资源

backend.ip = 192.168.31.141:8081,192.168.31.141:8082 
# 后端ip 改为自己的后端 ip

backend.value = 100,100 
# 权重

backend.loadbalancer = com.h.system.tinynignx.loadbalance.RoundRobinByWeightLoadBalance
## 负载均衡的类 默认为roundrobin 

frontend.url = /home/hou/IdeaProjects/TinyNginx/src/main/resources/dist 
## 前端目录
```

5. 测试

前端：浏览器输入 http://localhost:8081/index.html

![3.png](static%2F3.png)

后端：在postman上 发送post请求 http://localhost:8081/webapp/home/dologin

![4.png](static%2F4.png)
## 负载均衡的介绍
负载均衡器在实际的生
## server的实现
## client+连接池的实现
## 负载均衡算法的实现
### 扩展负载均衡算法
## 高并发的实现
## 整合Prometheus----实现根据负载动态上下线后端服务器
## 限流的实现
### 基于tps限流
### 基于并发数的限流
## 压测
## nginx与tomcat与rpc



  