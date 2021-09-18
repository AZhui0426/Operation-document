# SQL Server安装

系统：centos7.9

所需工具：docker

**dicker安装：**

https://blog.csdn.net/linhongwangok/article/details/109754207

配置阿里云镜像加速

针对Docker客户端版本大于 1.10.0 的用户，您可以通过修改daemon配置文件/etc/docker/daemon.json来使用加速器（没有则创建

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

**sql server安装：**

```
# docker下载安装
docker pull microsoft/mssql-server-linux:2017-latest 
# 说明
#-e ACCEPT_EULA = Y 设置ACCEPT_EULA变量为任何值，以确认你接受最终用户许可协议。 SQL Server 映像的必需设置。
#-e MSSQL_SA_PASSWORD =<YourStrong ！Passw0rd> 指定你自己的强密码至少 8 个字符并达到SQL Server 密码要求（大、小写+数字）。 SQL Server 映像的必需设置。
docker run -d -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=123456aA!' -p 1433:1433 --name sqlserver2017 -v /var/lib/mssql_data:/opt/mssql_data microsoft/mssql-server-linux:2017-latest

# ！！！进入内部 开启代理！！！
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
WHERE NAME = 'YOUR.DATABASE.NAME'

开启CDC功能
# 使用指定库
use 'YOUR.DATABASE.NAME'
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

--如果不想控制访问角色，则@role_name必须显式设置为null。
```



# Spring Boot API 监控 SQL Server

```java
public static void main(String[] args) {
    // 1. 生成配置
    Properties props = genProps();
    // 2. 业务处理逻辑部分代码
    DebeziumEngine<ChangeEvent<String, String>> engine = engineBuild(props);
    // 3. 正式运行
    runSoftware(engine);
}

// 开始运行程序
public static void runSoftware(DebeziumEngine<ChangeEvent<String, String>> engine) {
    //线程池，开新线程异步执行。
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(engine);
    try {
        //60s后尝试关闭线程，因为线程内任务未完成，线程关闭失败。
        //执行engine.close，关闭Debezium引擎，线程关闭成功。
        while (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
            engine.close();
        }
    } catch (InterruptedException | IOException e) {
        Thread.currentThread().interrupt();
    }
}

// 实现逻辑
public static DebeziumEngine<ChangeEvent<String, String>> engineBuild(Properties props) {

    // 2. 构建 DebeziumEngine
    // 使用 Json 格式
    DebeziumEngine<ChangeEvent<String, String>> engine =
        DebeziumEngine.create(Json.class)
        .using(props)
        .notifying(record -> {
            // record中会有操作的类型（增、删、改）和具体的数据
            // key是主键
            System.out.println("record.key() = " + record.key());
            System.out.println("record.value() = " + record.value());

        })
        .using((success, message, error) -> {
            // 强烈建议加上此部分的回调代码，方便查看错误信息

            if (!success && error != null) {
                // 报错回调
                System.out.println("----------error------");
                System.out.println(message);
                System.out.println(error);
                System.out.println(success);
            }
        }).build();
    return engine;
}

private static Properties genProps() {
    // 配置
    Properties props = new Properties();

    //  连接器的Java类名称
    props.setProperty("connector.class", SqlServerConnector.class.getName());
    // 偏移量持久化，用来容错 默认值
    props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
    // 要存储偏移量的文件路径 默认/tmp/offsets.dat  如果路径配置不正确可能导致无法存储偏移量 可能会导致重复消费变更
    // 如果连接器重新启动，它将使用最后记录的偏移量来知道它应该恢复读取源信息中的哪个位置。
    props.setProperty("offset.storage.file.filename", "D:\\temp\\sqlserver\\offset.txt");
    // 尝试提交偏移量的时间间隔。默认值为 1分钟
    props.setProperty("offset.flush.interval.ms", String.valueOf(6000L));
    // 监听连接器实例的 唯一名称
    props.setProperty("name", "sqlserver_1");
    // IP
    props.setProperty("database.hostname", "YOUR.HOST");
    // SQL Server 实例的端口号
    props.setProperty("database.port", String.valueOf(1433));
    // SQL Server 用户的名称
    props.setProperty("database.user", "sa");
    // SQL Server 用户的密码
    props.setProperty("database.password", "YOUR.PASSWORD");
    // 要从中捕获更改的数据库的名称
    props.setProperty("database.dbname", "debezium");
    // Debezium 应捕获其更改的所有表的列表，如有多张表则用“，”隔开。
    props.setProperty("table.include.list", "dbo.student");
    // SQL Server 实例/集群的逻辑名称，形成命名空间，用于连接器写入的所有 Kafka 主题的名称、Kafka Connect 架构名称以及 Avro 转换器时对应的 Avro 架构的命名空间用来
    props.setProperty("database.server.name", "my_server_1"); // 可以任意修改，但必须唯一
    props.setProperty("database.history", FileDatabaseHistory.class.getCanonicalName());
    props.setProperty("database.history.file.filename", "D:\\temp\\sqlserver\\dbhistory.dat");

    return props;
}
```



# Connector 监控 SQL Server