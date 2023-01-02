**自定义注解+AOP记录日志**（技术亮点）



RequestContextHolder 工具类获取request对象



> ```
> 获取request 请求工具类
> RequestContextHolder 是Spirng提供的一个用来暴漏Request对象的工具，利用RequestContextHolder，可以在一个请求线程中获取到Request，避免了Request从投传到尾的情况
> ```



Ip工具类获取 远程ip地址



图片上传到七牛云，降低服务器访问压力



### 踩坑

1. Long类型id 太长传到前端 无法识别
2. FROM_UNIXTIME 查询月份用 %c 而不是 %m(月份小于10为 01, 02, 03...)



### 亮点

![image-20230102120059301](https://tudoutiaoya-notes.oss-cn-beijing.aliyuncs.com/img/image-20230102120059301.png)



使用mq解决缓存不一致问题

修改文章后，能够保证最终一致性

删除文章列表缓存、更新查看某个文章的缓存



部署：加上/api区分  前端的访问与后端的访问



