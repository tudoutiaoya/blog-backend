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





## 应用技术

SpringBoot + Redis + Mysql + Swagger + 七牛云



## 学到东西

1. Swagger接口文档
2. 统一异常处理，捕获Controller层的异常
3. 登录拦截器，对指定接口进行拦截 从redis中获取token以及用户信息，判断是否登录
4. 登录拦截器从redis获取到用户，放入到ThreadLocal中，方便在请求内部获取用户信息，减少了用户信息远程查询次数
5. 登录拦截器中使用完ThreadLocal做了value的删除，防止了内存泄漏
6. JWT + redis token令牌登录方式，登录用户做了缓存， 灵活控制用户的过期（提掉线）
7. 对接七牛云对象存储，做了CND加速
8. 查看文章详情，阅读数量增加 扔到线程池中执行，不影响主业务逻辑























