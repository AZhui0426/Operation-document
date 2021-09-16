Prometheus 下载与安装

前言

下载地址：https://prometheus.io/download/

版本

JDK：jdk1.8

Prometheus：prometheus-2.27.1.linux-amd64.tar.gz

一、下载解压后，进入解压目录

tar -zxvf prometheus-2.27.1.linux-amd64.tar.gz

二、编写prometheus.yml文件

vim prometheus.yml

修改访问ip及端口，默认为localhost:9090

三、启动prometheus服务

nohup ./prometheus &

访问http://localhost:9090 可以进入到prometheus界面

登录账号: admin

初始密码：admin