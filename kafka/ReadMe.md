kafka搭建与，包与配置文件,监控的搭建



# Kafka

系统版本：ubuntu18.04

kafka版本：kafka_2.12-2.7.0

官网下载地址：http://kafka.apache.org/downloads

对应版本下载链接：https://archive.apache.org/dist/kafka/2.7.0/kafka_2.12-2.7.0.tgz

## Kafka单机

前置需求：安装jdk、启用zookeeper。

### 一、解压kafka

这里将kafka解压到 /usr/local/下

```
tar -zxvf ./kafka_2.12-2.7.0.tar.gz –C /usr/local/
```

### 二、配置kafka

配置文件为/usr/local/kafka_2.12-2.7.0/config/server.properties

```
//参考指令
sudo vim /usr/local/kafka_2.12-2.7.0/config/server.properties

//核心配置内容修改
//zookeeper连接端口的更改。
zookeeper.connect=host:port
//例如
zookeeper.connect=localhost:2181
```

配置项含义参考：

[apache kafka系列之server.properties配置文件参数说明](https://blog.csdn.net/lizhitao/article/details/25667831)

### 三、启动kafka

```
//进入kafka文件夹
cd /usr/local/kafka_2.12-2.7.0

//启动kafka
sudo ./bin/kafka-server-start.sh ./config/server.properties
//挂起启动kafka
sudo nohup ./bin/kafka-server-start.sh ./config/server.properties
```

**如下提示怎么办？**

```
[2021-08-03 18:07:26,633] INFO Socket error occurred: localhost/127.0.0.1:2181: Connection refused (org.apache.zookeeper.ClientCnxn)
[2021-08-03 18:07:27,735] INFO Opening socket connection to server localhost/127.0.0.1:2181. Will not attempt to authenticate using SASL (unknown error) (org.apache.zookeeper.ClientCnxn)
```

该问题为，kafka配置文件server.properties中  **zookeeper.connect=host:port**  所配置的端口并没有zookeeper服务。

**如何开启kafka自带zookeeper？**

```
# 配置zookeeper配置文件
vim ./config/zookeeper.properties
# 配置zookeeper数据存放文件夹
dataDir=/tmp/zookeeper
# 配置zookeeper日志文件存放路径（需要手动添加
dataLogDir=/tmp/zookeeper/log
# zookeeper启动端口（默认为2181
clientPort=2181

# 开启kafka自带zookeeper
# 前为zookeeper启动文件，后为zookeeper配置文件
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
# 后台启动
nohup ./bin/zookeeper-server-start.sh ./config/zookeeper.properties
```



## Kafka集群

前置需求：安装jdk、启用zookeeper集群。

### 一、准备多台服务器，并分别搭建kafka

参考：

```
//kafka1
192.168.1.60:9092
//kafka2
192.168.1.61:9092
//kafka3
192.168.1.62:9092
```

### 二、配置

配置文件为/usr/local/kafka_2.12-2.7.0/config/server.properties

```
//参考指令
sudo vim /usr/local/kafka_2.12-2.7.0/config/server.properties

//核心配置内容修改
//集群内各个kafka的该配置项请勿重复！！
broker.id=0
//此处为kafka的host、端口。
advertised.listeners=PLAINTEXT://192.168.1.60:9092
//zookeeper连接端口的更改,写入所有zookeeper集群包含的。
zookeeper.connect=host:port,host:port,host:port
//例如
zookeeper.connect=localhost:2181

//重要！！
//重要！！
//重要！！
//用于配置offset记录的topic的partition的副本个数，默认为1。建议修改成kafka集群中kafka服务器个数。
offsets.topic.replication.factor=3
//事务主题的复制因子。建议修改成kafka集群中kafka服务器个数。
transaction.state.log.replication.factor=3
//覆盖事务主题的min.insync.replicas配置。建议修改成kafka集群中kafka服务器个数。
transaction.state.log.min.isr=3
//自动创建topic时的默认副本的个数。建议修改成kafka集群中kafka服务器个数。
default.replication.factor=3
```

“重要！！“下方修改的配置，是为了防止kafka集群中，任意kafka服务器宕机，导致消费发生异常。

注意！default.replication.factor=3只对配置生效后创建的topic有效！



提示：

如果在更改配置文件前，topic就创建了，则可以先删除该topic，再进行创建。

```
//删除指令，在kafka文件夹下
./bin/kafka-topics.sh --describe --zookeeper zkHost:zkPort --topic topicName
```

### 三、分别启动集群内的各个kafka

```
//进入kafka文件夹
cd /usr/local/kafka_2.12-2.7.0

//启动kafka
sudo ./bin/kafka-server-start.sh ./config/server.properties
//挂起启动kafka
sudo nohup ./bin/kafka-server-start.sh ./config/server.properties
```



# 监控kafka

以下提供了jmx与kafka_exporter监控kafka的两种方法，两种监控方法在metrics中，数据的命名规则不同，grafana模板无法互相使用，请注意使用对应的grafana模板。

我们这里主要使用jmx监控kafka。

## 一、JMX_exporter监控Kafka

JMX监控需要在linux上已经安装好jdk。

系统版本：ubuntu18.04、centos7

jmx_prometheus_javaagent版本：0.15.0

kafka版本：kafka_2.12-2.7.0

参照git：

[GitHub - prometheus/jmx_exporter: A process for exposing JMX Beans via HTTP for Prometheus consumption](https://github.com/prometheus/jmx_exporter)

以下流程全部参考该git进行。

### 一、下载jmx_prometheus_javaagent

下载地址：

https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.15.0/jmx_prometheus_javaagent-0.15.0.jar

放到kafka路径下。

参考路径：

```
kafka: /usr/local/kafka_2.12-2.7.0
jar包：/usr/local/kafka_2.12-2.7.0/jvm/jmx_prometheus_javaagent-0.15.0.jar
```

### 二、添加配置文件

这里直接使用的git中javaagents的示例配置。

地址：https://github.com/prometheus/jmx_exporter/tree/master/example_configs

选择的是其中的：kafka-2_0_0.yml

地址：https://github.com/prometheus/jmx_exporter/blob/master/example_configs/kafka-2_0_0.yml

参考存放路径：

```
# 这里示例中改名为了kafka.yml，可以不改名
/usr/local/kafka_2.12-2.7.0/jvm/kafka.yml
```

| Name                      | Description                                                  |
| ------------------------- | ------------------------------------------------------------ |
| startDelaySeconds         | start delay before serving requests. Any requests within the delay period will result in an empty metrics set. |
| hostPort                  | The host and port to connect to via remote JMX. If neither this nor jmxUrl is specified, will talk to the local JVM. |
| username                  | The username to be used in remote JMX password authentication. |
| password                  | The password to be used in remote JMX password authentication. |
| jmxUrl                    | A full JMX URL to connect to. Should not be specified if hostPort is. |
| ssl                       | Whether JMX connection should be done over SSL. To configure certificates you have to set following system properties: `-Djavax.net.ssl.keyStore=/home/user/.keystore` `-Djavax.net.ssl.keyStorePassword=changeit` `-Djavax.net.ssl.trustStore=/home/user/.truststore` `-Djavax.net.ssl.trustStorePassword=changeit` |
| lowercaseOutputName       | Lowercase the output metric name. Applies to default format and `name`. Defaults to false. |
| lowercaseOutputLabelNames | Lowercase the output metric label names. Applies to default format and `labels`. Defaults to false. |
| whitelistObjectNames      | A list of [ObjectNames](http://docs.oracle.com/javase/6/docs/api/javax/management/ObjectName.html) to query. Defaults to all mBeans. |
| blacklistObjectNames      | A list of [ObjectNames](http://docs.oracle.com/javase/6/docs/api/javax/management/ObjectName.html) to not query. Takes precedence over `whitelistObjectNames`. Defaults to none. |
| rules                     | A list of rules to apply in order, processing stops at the first matching rule. Attributes that aren't matched aren't collected. If not specified, defaults to collecting everything in the default format. |
| pattern                   | Regex pattern to match against each bean attribute. The pattern is not anchored. Capture groups can be used in other options. Defaults to matching everything. |
| attrNameSnakeCase         | Converts the attribute name to snake case. This is seen in the names matched by the pattern and the default format. For example, anAttrName to an_attr_name. Defaults to false. |
| name                      | The metric name to set. Capture groups from the `pattern` can be used. If not specified, the default format will be used. If it evaluates to empty, processing of this attribute stops with no output. |
| value                     | Value for the metric. Static values and capture groups from the `pattern` can be used. If not specified the scraped mBean value will be used. |
| valueFactor               | Optional number that `value` (or the scraped mBean value if `value` is not specified) is multiplied by, mainly used to convert mBean values from milliseconds to seconds. |
| labels                    | A map of label name to label value pairs. Capture groups from `pattern` can be used in each. `name` must be set to use this. Empty names and values are ignored. If not specified and the default format is not being used, no labels are set. |
| help                      | Help text for the metric. Capture groups from `pattern` can be used. `name` must be set to use this. Defaults to the mBean attribute description and the full name of the attribute. |
| cache                     | Whether to cache bean name expressions to rule computation (match and mismatch). Not recommended for rules matching on bean value, as only the value from the first scrape will be cached and re-used. This can increase performance when collecting a lot of mbeans. Defaults to `false`. |
| type                      | The type of the metric, can be `GAUGE`, `COUNTER` or `UNTYPED`. `name` must be set to use this. Defaults to `UNTYPED`. |

### 三、配置kafka-server-start.sh文件

参考配置方法：

```
cd /usr/local/kafka_2.12-2.7.0/bin/						//进入文件所在路径
sudo vim ./kafka-server-start.sh						//使用管理员权限修改文件
```

添加代码：

```
# 指定9999端口暴露出来供采集
export JMX_PORT="9999"

# 配置jar包以及配置文件的路径，9990为设置的jmx提供数据的端口
# 请注意文件名正确，这里路径最后为kafka.yml是因为对kafka-2_0_0.yml更名为kafka.yml
export KAFKA_OPTS="-javaagent:/usr/local/kafka_2.12-2.7.0/jvm/jmx_prometheus_javaagent-0.15.0.jar=9990:/usr/local/kafka_2.12-2.7.0/jvm/kafka.yml"
```

正常启动kafka，即可通过9990端口拿到metrics

```
cd /usr/local/kafka_2.12-2.7.0
sudo ./bin/kafka-server-start.sh ./config/server.properties
```



## 二、kafka_exporter

系统版本：ubuntu18.04、centos7

exporter版本：kafka_exporter-1.3.1.linux-amd64

参照git：

[GitHub - danielqsj/kafka_exporter: Kafka exporter for Prometheus](https://github.com/danielqsj/kafka_exporter/)

### 一、解压kafka_exporter

```
tar -zxvf kafka_exporter-1.3.1.linux-amd64.tar.gz
```

### 二、启动kafka_exporter

核心指令：

进入kafka_exporter文件夹进行操作

```
nohup ./kafka_exporter --kafka.server=kafkaHost:kafkaPort
```

参考指令：

```
sudo nohup ./kafka_exporter --kafka.server=192.168.1.64:9092	//9092为kafka默认端口
```

更多flag参考顶部github网址，或使用 -h/-help

```
./kafka_exporter -h
```

### 三、查看是否启动成功

访问metrics网址：

```
//9308为默认端口号
host:9308/metrics
```

