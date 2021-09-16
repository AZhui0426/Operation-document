mongodb搭建与，包与配置文件,监控

# mongodb_exporter

系统版本：ubuntu18.04

exporter版本：mongodb_exporter-0.20.5.linux-amd64

参照git：

[GitHub - percona/mongodb_exporter: A Prometheus exporter for MongoDB including sharding, replication and storage engines](https://github.com/percona/mongodb_exporter)

### 一、解压mongodb_exporter

```
tar -zxvf mongodb_exporter-0.20.5.linux-amd64.tar.gz
```

### 二、测试mongodb

```
crul hort:port
出现
It looks like you are trying to access MongoDB over HTTP on the native driver port.
则mongodb启动与连接均正常
```

### 三、启动mongodb_exporter

核心指令：

```
nohup ./mongodb_exporter --mongodb.uri=mongodb://user:pass@host:port/tablename?ssl=true
```

参考指令：

```
sudo nohup ./mongodb_exporter --mongodb.uri=mongodb://root:shenlandt2018@81.69.96.245:27017/admin --compatible-mode
```

可能出现：

```
ERRO[0043] Cannot connect to MongoDB: error parsing uri: unescaped colon in password
```

可能是没有添加：

```
--compatible-mode //兼容新老版本的信息导出
```

更多flag参考顶部github网址

### 四、查看是否启动成功

```
ps -ef | grep mongodb_exporter
```

成功时提示示例：

```
root     28087 27629  0 14:02 pts/6    00:00:00 sudo ./mongodb_exporter --mongodb.uri=mongodb://user:pass@host:port/tablename --compatible-mode
root     28088 28087  0 14:02 pts/6    00:00:01 ./mongodb_exporter --mongodb.uri=mongodb://user:pass@host:port/tablename --compatible-mode
guomaof+ 28240 28219  0 14:04 pts/9    00:00:00 grep --color=auto mongodb_exporter
```

也可以直接访问metrics网址测试：

```
//9216为默认端口号
host:9216/metrics
```
