# Linux版

**前置条件：jdk1.8**

**系统版本：centos 6.9**

**elasticsearch版本：6.3.2**

### 1.下载

https://www.elastic.co/cn/downloads/past-releases/elasticsearch-6-3-2

点击蓝色 【TAR】 开始下载。

### 2.通过FTP上传至Linux服务器

### 3.登录root用户！！！！

### 4.解压

```linux
# 解压缩
tar -zxvf elasticsearch-6.3.2.tar.gz
# 创建文件夹
mkdir /opt/module
mkdir /opt/module/es
# 移动文件
mv elasticsearch-6.3.2 /opt/module/es/
```

### 5.创建用户

因为安全问题， Elasticsearch 不允许 root 用户直接运行，所以要创建新用户，在 root 用户中创建新用户。

```
useradd shenlan #新增 es 用户
passwd shenlan #为 es 用户设置密码
userdel -r shenlan #如果错了，可以删除再加
#文件夹所有者
chown -R shenlan:shenlan /opt/module/es/elasticsearch-6.3.2/
```

### 6.修改配置文件

操作文件指南：
方向键移动光标
按 i ，即可进入插入文本模式
按 shift+: 再输入 指令 回车，执行相应指令。
q 退出。 wq 保存并退出

```
# 打开并修改文本
vim /opt/module/es/elasticsearch-6.3.2/config/elasticsearch.yml
# 按 i 编辑文本
# 加入如下配置
node.name: node-1
network.host: 0.0.0.0
http.port: 9201
# shift+： 并输入 wq 回车
```

### 7.直接启动

会出现错误：max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]

修改/etc/security/limits.conf

```
vim /etc/security/limits.conf

# 在文件末尾中增加下面内容
# 每个进程可以打开的文件数的限制
es soft nofile 65536
es hard nofile 65536
```

### 8.修改/etc/sysctl.conf

```
vim /etc/sysctl.conf

# 在文件中增加下面内容
# 一个进程可以拥有的 VMA(虚拟内存区域)的数量,默认值为 65536
vm.max_map_count=655360
# shift+： 输入 wq 回车
# 重新加载
sysctl -p
```

### 9.启动es

```
#启动
bin/elasticsearch
#后台启动
bin/elasticsearch -d
```



# Windows版

**前置条件：jdk1.8**

**elasticsearch版本：6.3.2**

### 1.下载

https://www.elastic.co/cn/downloads/past-releases/elasticsearch-6-3-2

点击蓝色 【TAR】 开始下载。

### 2.解压

解压到任意位置均可，建议非系统盘。

### 3.修改配置文件

```
# 打开并修改文本 文件目录elasticsearch-6.3.2/config/elasticsearch.yml
# 加入如下配置
node.name: node-1
network.host: 0.0.0.0
http.port: 9201
```

### 4.直接启动

双击文件：elasticsearch-6.3.2\bin\elasticsearch.bat