Zookeeper 下载与安装

前言

下载地址：https://zookeeper.apache.org/releases.html

版本

JDK：jdk1.8

Zookeeper：apache-zookeeper-3.7.0-bin

Zookeeper 简单命令

启动：./zkServer.sh start

停止：./zkServer.sh stop

查看 ZK 状态：./zkServer.sh status

使用 zkCli 连接：./zkCli.sh -server 127.0.0.1:2181

单机安装

一、下载解压后，进入解压目录

tar -zxvf apache-zookeeper-3.7.0-bin.tar.gz

解压后，

创建目录用于存放快照

mkdir /home/guomaofei /zookeeper/data

创建目录用于存放日志

mkdir /home/guomaofei /zookeeper/log

二、从 conf 目录下拷贝 zoo_sample.cfg 到该目录下并重命名为 zoo.cfg

cp zoo_sample.cfg /home/guomaofei/zookeeper/apache-zookeeper-3.7.0-bin/conf/zoo.cfg

三、配置 zoo.cfg ，指定数据目录和日志目录

vim zoo.cfg

修改dataDir=/home/guomaofei/zookeeper/data

添加dataLogDir=/home/guomaofei/zookeeper/log

四、进入 bin 目录，执行命令启动 zk，查看 zk 状态

启动命令

./zkServer.sh start

查看启动状态命令

./zkServer.sh status

五、进入 bin 目录使用 zkCli 连接本地 zk

连接命令

./zkCli.sh -server 127.0.0.1:2181

集群安装

需要安装奇数个 zookeeper 服务

一、下载解压后，进入解压目录

二、从 conf 目录下拷贝 zoo_sample.cfg 到该目录下并重命名为 zoo.cfg

三、配置 zoo.cfg ，指定数据目录和日志目录

前三步与单机安装一致

四、配置多个 zk 节点

vim zoo.cfg

添加zk节点

server.1=node1:2888:3888

server.2=node2:2888:3888

server.3=node3:2888:3888

node1，node2,node3为服务器的IP

五、在 dataDir 指定目录下，为每个 zk 节点配置 myid

vim myid

在node1机器中，这个文件的内容是1

在node2机器中，这个文件的内容是2

在node3机器中，这个文件的内容是3

六、进入 bin 目录，执行命令启动 zk，查看 zk 状态

prometheus监控zookeeper

一、配置zookeeper的zoo.cfg文件，指定监控端口

vim zoo.cfg

添加监控节点端口

metricsProvider.className=org.apache.zookeeper.metrics.prometheus.PrometheusMetricsProvider

metricsProvider.httpPort=7000

metricsProvider.exportJvmInfo=true

zookeeper集群则为每台服务器都加上监控节点

重启zookeeper

二、配置prometheus的prometheus.yml文件，添加监控节点

vim prometheus.yml

添加监控节点（单机）

  - job_name: 'zookeeper'
  
    static_configs:
    
    - targets: ['node1:7070']
    
添加监控节点（集群）

  - job_name: 'zookeeper'
  
    static_configs:
    
    - targets: ['node1:7070','node2:7070','node3:7070']
    
node1，node2,node3为zookeeper集群服务器的IP

重启Prometheus

三、验证结果

进入prometheus页面，点开status下的targets，查看名称为zookeeper，状态为up代表成功监控