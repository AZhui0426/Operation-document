### hazelcast_exporter 安装使用

#### 1.hazelcast 版本 3.12.7

#### 2.hazelcast promethesue使用

1. maven连接到公司私服 

   ```xml
   <mirror>
       <id>shenlan-public</id>
       <mirrorOf>*</mirrorOf>
       <name>shenlan-public</name>
       <url>http://81.69.96.245:8081/repository/shenlan-public/</url>
   </mirror>
   ```

2. 使用客户端

```xml
<!-- The client -->
<dependency>
    <groupId>com.shenlan.prometheus.client.httpserver</groupId>
    <artifactId>http_server</artifactId>
    <version>1.0.0.ALPHA</version>
</dependency>
```

3.在代码中使用

```java
HazelcastUsingAllCollector hazelcastUsingAll = new HazelcastUsingAllCollector(hazelcastInstance);//将hazelcast实例传入
hazelcastUsingAll.startAll();//使用当前客户端中的所有collector
WebServer webServer = new WebServer("0.0.0.0", 17002);//监控启动服务和端口
webServer.startPrometheusClientServer();//启动服务
```