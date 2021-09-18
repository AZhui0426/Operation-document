# Kafka-connect

原文链接：https://blog.csdn.net/u011687037/article/details/57411790

### kafka connect的启动。


Kafka connect的工作模式分为两种，分别是**standalone**模式和**distributed**模式。

#### standalone启动：

```
bin/connect-standalone.sh config/connect-standalone.properties connector1.properties[connector2.properties ...]
```

一次可以启动多个connector，只需要在参数中加上connector的配置文件路径即可。

#### distributed启动：

```
bin/connect-distributed.sh config/connect-distributed.properties
```

启动不需要传递connector的参数，而是通过REST API来对kafka connect进行管理，包括启动、暂停、重启、恢复和查看状态的操作，具体介绍详见下文。

在启动kafkaconnect的distributed模式之前，首先需要创建三个主题，这三个主题的配置分别对应connect-distributed.properties文件中config.storage.topic(default connect-configs)、offset.storage.topic (default connect-offsets) 、status.storage.topic (default connect-status)的配置，那么它们分别有啥用处呢？

config.storage.topic：用以保存connector和task的配置信息，需要注意的是这个主题的分区数只能是1，而且是有多副本的。（推荐partition 1，replica 3）
offset.storage.topic:用以保存offset信息。（推荐partition50，replica 3）
status.storage.topic:用以保存connetor的状态信息。（推荐partition10，replica 3）


### 通过rest api管理connector

因为kafka connect的意图是以服务的方式去运行，所以它提供了REST API去管理connectors，默认的端口是8083，你也可以在启动kafka connect之前在配置文件中添加rest.port配置。

GET /connectors – 返回所有正在运行的connector名
POST /connectors – 新建一个connector; 请求体必须是json格式并且需要包含name字段和config字段，name是connector的名字，config是json格式，必须包含你的connector的配置信息。
GET /connectors/{name} – 获取指定connetor的信息
GET /connectors/{name}/config – 获取指定connector的配置信息
PUT /connectors/{name}/config – 更新指定connector的配置信息
GET /connectors/{name}/status – 获取指定connector的状态，包括它是否在运行、停止、或者失败，如果发生错误，还会列出错误的具体信息。
GET /connectors/{name}/tasks – 获取指定connector正在运行的task。
GET /connectors/{name}/tasks/{taskid}/status – 获取指定connector的task的状态信息
PUT /connectors/{name}/pause – 暂停connector和它的task，停止数据处理知道它被恢复。
PUT /connectors/{name}/resume – 恢复一个被暂停的connector
POST /connectors/{name}/restart – 重启一个connector，尤其是在一个connector运行失败的情况下比较常用
POST /connectors/{name}/tasks/{taskId}/restart – 重启一个task，一般是因为它运行失败才这样做。
DELETE /connectors/{name} – 删除一个connector，停止它的所有task并删除配置。