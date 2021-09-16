### Springboot 开启prometheus监控

1.添加Springboot监控模块

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

2.yml配置

```yml
management:
  endpoints:
    web:
      exposure:
        include: 'prometheus'
  metrics:
    tags:
      application: ${spring.application.name}
```

3. prometheus 配置

   ~~~yml
   scrape_configs:
     - job_name: "data-server"
       #指定监控路径
       metrics_path: "/ids/actuator/prometheus"
       static_configs:
       #指定监控地址端口
       - targets: ["127.0.0.1:13000"，"**********:1200"]
   ~~~

   

