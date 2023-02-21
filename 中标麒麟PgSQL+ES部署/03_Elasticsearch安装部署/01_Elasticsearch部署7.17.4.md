# Linux版

**前置条件：jdk1.8 & jdk1.11**

**系统版本：centos 6.9**

**elasticsearch版本：7.17.4**

### 1.下载

https://www.elastic.co/cn/downloads/past-releases/elasticsearch-7-17-4

点击Linux X86_64进行下载

### 2.通过FTP上传至Linux服务器

### 3.登录root用户！！！！

### 4.解压

```linux
# 解压缩
tar -zxvf elasticsearch-7.17.4-linux-x86_64.tar.gz
# 创建文件夹
mkdir /opt/module
mkdir /opt/module/es
# 移动文件
mv elasticsearch-7.17.4 /opt/module/es/
```

### 5.创建用户

因为安全问题， Elasticsearch 不允许 root 用户直接运行，所以要创建新用户，在 root 用户中创建新用户。

```
useradd shenlan #新增 es 用户
passwd shenlan #为 es 用户设置密码
userdel -r shenlan #如果错了，可以删除再加
#文件夹所有者权限赋予
chown -R shenlan:shenlan /opt/module/es/elasticsearch-7.17.4/
```

### 6.修改配置文件

操作文件指南：
方向键移动光标
按 i ，即可进入插入文本模式
按 shift+: 再输入 指令 回车，执行相应指令。
q 退出。 wq 保存并退出

```
# 打开并修改文本
vi /opt/module/es/elasticsearch-7.17.4/config/elasticsearch.yml
# 按 i 编辑文本
# 加入如下配置 节点名称，节点端口，初始集群主机节点
node.name: node-1
network.host: 0.0.0.0
http.port: 9201
cluster.initial_master_nodes: ["node-1"]
# shift+： 并输入 wq 回车
```

### 7.修改limits.conf

修改/etc/security/limits.conf

```
vi /etc/security/limits.conf

# 在文件末尾中增加下面内容
# 每个进程可以打开的文件数的限制 开头shenlan为运行ES的用户名
shenlan soft nofile 65536
shenlan hard nofile 65536
```

### 8.修改/etc/sysctl.conf

```
vi /etc/sysctl.conf

# 在文件中增加下面内容
# 一个进程可以拥有的 VMA(虚拟内存区域)的数量,默认值为 65536
vm.max_map_count=655360
# shift+： 输入 wq 回车
# 重新加载 如果权限不足 请检查是否为root用户
sysctl -p
```

### 9.启动es

```
#切换启动用户
su shenlan
#启动
/opt/module/es/elasticsearch-7.17.4/bin/elasticsearch
#如果启动正常 ctrl+c退出程序
#后台启动
/opt/module/es/elasticsearch-7.17.4/bin/elasticsearch -d
```



# Windows版

**前置条件：jdk1.8**

**elasticsearch版本：7.17.4**

### 1.下载

https://www.elastic.co/cn/downloads/past-releases/elasticsearch-7.17.4

点击蓝色 【WINDOWS】 开始下载。

### 2.解压

解压到任意位置均可，建议非系统盘。

### 3.修改配置文件

```
# 打开并修改文本 文件目录elasticsearch-7.17.4/config/elasticsearch.yml
# 加入如下配置
node.name: node-1
network.host: 0.0.0.0
http.port: 9201
```

### 4.直接启动

双击文件：elasticsearch-7.17.4\bin\elasticsearch.bat

