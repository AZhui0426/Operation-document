## 安装docker

官方安装文档

https://docs.docker.com/engine/install/centos/

参考CSDN

https://blog.csdn.net/linhongwangok/article/details/109754207

菜鸟教程

https://www.runoob.com/docker/docker-tutorial.html

配置阿里云镜像加速

针对Docker客户端版本大于 1.10.0 的用户

您可以通过修改daemon配置文件/etc/docker/daemon.json来使用加速器

```
mkdir -p /etc/docker
tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://v2ltjwbg.mirror.aliyuncs.com"]
}
EOF
systemctl daemon-reload
systemctl restart docker
```



## docker 安装 sqlserver

#### spring boot + debezium 监控SQL server

https://blog.csdn.net/leilei1366615/article/details/118465712

包去官网下载

https://repo1.maven.org/maven2/io/debezium/

```
docker pull microsoft/mssql-server-linux:2017-latest 
# 安装
# 说明
#-e ACCEPT_EULA = Y 设置ACCEPT_EULA变量为任何值，以确认你接受最终用户许可协议。 SQL Server 映像的必需设置。

#-e MSSQL_SA_PASSWORD =<YourStrong ！Passw0rd> 指定你自己的强密码至少 8 个字符并达到SQL Server 密码要求。 SQL Server 映像的必需设置。
docker run -d -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=123456aA!' -p 1433:1433 --name sqlserver2017 -v /var/lib/mssql_data:/opt/mssql_data microsoft/mssql-server-linux:2017-latest

# 进入内部 开启代理  
docker exec -it sqlserver2017 "bash"
# 内部执行以下命令
/opt/mssql/bin/mssql-conf set sqlagent.enabled true
# 退出容器
exit
# 重启容器
docker restart sqlserver2017

#------以下操作可直接在Navicat中连接到数据库后执行
# 查看指定SQL SERVER数据库是否开启CDC功能
SELECT is_cdc_enabled,CASE WHEN is_cdc_enabled=0 THEN 'CDC off' ELSE 'CDC on' END 描述
FROM sys.databases
WHERE NAME = 'GPS'

开启CDC功能
# 使用指定库
use gps
# 执行命令
EXECUTE sys.sp_cdc_enable_db

#查看当前已经开启CDC的数据表
SELECT name,is_tracked_by_cdc FROM sys.tables WHERE is_tracked_by_cdc = 1;
开启表CDC
示例：

对'dbo.vehicle'表开启变更捕获

EXEC sys.sp_cdc_enable_table
@source_schema= 'dbo',      --源表架构
@source_name = 'vehicle',  --源表
@role_name = 'CDC_Role'     --角色（将自动创建）
GO

--如果不想控制访问角色，则@role_name必须显式设置为null。

```

