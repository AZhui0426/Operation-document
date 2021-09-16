# Linux的监控

参考：

https://www.cnblogs.com/xiangsikai/p/11289157.html

- **使用版本**
- node_exporter 0.17.0

- **相关文档**
- 使用文档：https://prometheus.io/docs/guides/node-exporter/
- GitHub：https://github.com/prometheus/node_exporter
- exporter列表：https://prometheus.io/docs/instrumenting/exporters/

- **百度云下载：node_exporter 0.17.0**
- 地址：https://pan.baidu.com/s/1kXKs6oiGFwubsJ-cGvWykg
- 密码：vrpk

 

**安装监控客户端**

**1、下载到被监控的linux系统**

```
下载地址：https://github.com/prometheus/node_exporter/releases/download/v0.18.1/node_exporter-0.18.1.linux-amd64.tar.gz
```

**2、解压压缩包**

```
tar -zxvf node_exporter-0.17.0.linux-amd64.tar.gz 
```

**3、移动并进入目录**

```
mv node_exporter-0.17.0.linux-amd64 /usr/local/node_exporter
cd /usr/local/node_exporter
```

**4、启动node_exporter服务，默认9100端口**

```
nohup ./node_exporter
```



#### 第5步第6步选用！第5步第6步选用！第5步第6步选用！（这不是必须的！

**5、添加系统服务：vi /usr/lib/systemd/system/node_exporter.service**

```
[Unit]
Description=https://prometheus.io

[Service]
Restart=on-failure
ExecStart=/usr/local/node_exporter/node_exporter --conllector.systemd --conllector.systemd.unit-whitelist=(docker|kubelet|kube-proxy|flanneld).service

[Install]
WantedBy=multi-user.target
```

**6、启动添加后的系统服务**

```
systemctl daemon-reload
systemctl restart node_exporter
```

**7、查看导出器导出的数据信息：http://47.98.138.176:9100/metrics**

![img](https://img2018.cnblogs.com/blog/1183448/201908/1183448-20190802160502602-2116139700.png)

------

**使用prometheus监控客户端实例**

**1、添加新的job服务发现获取新的node：vim prometheus.yml**

```
scrape_configs:
  -  .....
  - job_name: 'node'
    file_sd_configs:
        - files: ['/usr/local/prometheus/sd_config/node.yml']
          refresh_interval: 5s
```

**2、新建配置文件并添加node：vim /usr/local/prometheus/sd_config/node.yml**

```
- targets:
  - 47.98.138.176:9100
```

**3、检查并重新加载配置文件**

```
./promtool check config prometheus.yml
kill -hup PID
```