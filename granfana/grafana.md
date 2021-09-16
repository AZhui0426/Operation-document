Grafana 下载与安装

前言

下载地址：https://grafana.com/grafana/download

版本

JDK：jdk1.8

Linux版本

Grafana：grafana-8.0.0.linux-amd64.tar.gz

一、下载解压

wget https://dl.grafana.com/oss/release/grafana-8.0.0.linux-amd64.tar.gz

tar -zxvf grafana-8.0.0.linux-amd64.tar.gz

二、配置文件

/etc/grafana/grafana.ini

这里暂时保持默认配置即可

三、验证结果

启动grafana-server服务，并查看监听端口3000是否存在

systemctl start grafana-server

访问：http://ip:3000，默认帐号/密码：admin/admin

windows版本

grafana-7.5.7.windows-amd64.msi

一、下载安装

下载后安装，然后进入安装目录的conf文件夹，
将sample.ini复制一份，并改名为custom.ini，
在custom.ini里面可以做自己的配置，
这里我把端口号改为了http_port=9091

二、启动

进入bin目录，执行grafana-server.exe

三、验证

访问http://localhost:9091 进入Grafana的管理界面。默认账户密码为admin/admin。