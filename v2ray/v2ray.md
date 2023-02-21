# v2ray详细搭建教程

https://blog.codefat.cn/2020/11/15/v2ray%E8%AF%A6%E7%BB%86%E6%90%AD%E5%BB%BA%E4%BD%BF%E7%94%A8FanQ/



```
# start v2ray
systemctl start v2ray
# auto start v2ray
systemctl enable v2ray

   查看想开的端口是否已开：firewall-cmd --query-port=6379/tcp
   添加指定需要开放的端口：firewall-cmd --add-port=123/tcp --permanent
   重载入添加的端口：firewall-cmd --reload
   查询指定端口是否开启成功：firewall-cmd --query-port=123/tcp
   移除指定端口：firewall-cmd --permanent --remove-port=123/tcp

查看防火墙的状态：
https://zhuanlan.zhihu.com/p/447246725
```

-2hVfvMz}-y1aVz8

www.v2fly.org