# GateWay的操作指南

## 什么是路由网关？它存在意义又何在？

在微服务中，我们会有很多个微服务，但是客户端只有一个，它有调用我们远程接口的时候，就要使用不同的地址，比如下边三个微服务（也是本项目中出现的用到的三个微服务，获取代码地址见文末）

| 服务名       | 项目名             | 示例接口调用地址                       |
| ------------ | ------------------ | -------------------------------------- |
| 商品模块     | gateway-goods-7001 | ```http://localhost:7001/goods/main``` |
| 订单模块     | gateway-order-8001 | ```http://localhost:8001/order/main``` |
| 用户中心模块 | gateway-user-9001  | ```http://localhost:9001/user/main```  |

这样，客户端在调用的时候，还要使用不同的地址（主机名和端口），能不能就直接使用一个地址可以调用呢？就类似于我们之前写的一个war包那种项目？比如：

* ```http://localhost:80/goods/main``` 			  商品模块
* ```http://localhost:80/order/main``` 			  订单模块
* ```http://localhost:80/user/main``` 				用户中心模块

## GateWay的入门操作

* 引入依赖pom.xml

![](https://gitee.com/lyn4ever/picgo-img/raw/master/img/20200413225848.png)

* 配置application.yml

![](https://gitee.com/lyn4ever/picgo-img/raw/master/img/20200413225722.png)

* 主启动类

![](https://gitee.com/lyn4ever/picgo-img/raw/master/img/20200413225812.png)

好了，然后直接启动那三个微服务和这个项目，在浏览器中，就可以直接使用```http://localhost:80/goods/main```这样的地址访问各个服务了。

## GateWay的三大组件

###	路由（Route）

这个在上边的入门操作中已经演示过了，配置```spring.cloud.gateway.routes```来进行路由匹配。

但是，在上边我们配置的uri是网络地址（当然，可以直接通过我们的localhost配置到网络上其他的地址的，如baidu.com）。在微服务中，我们肯定是不能直接写死的，而且一个微服务也可能会有多个地址，所以，我们一定要配置成注册中心的微服务名。修改的配置内容如下：

![](https://gitee.com/lyn4ever/picgo-img/raw/master/img/20200413231418.png)

* 除了上述的application.yml外，还有一种方式配置路由（我猜你肯定不要说是applicatin.properties吧）。那就是通过注入SpringBean的方式，这里就不说，有兴趣的可以去官网看一下。但是建议你使用配置文件配置，因为可以集成Nacos进行动态配置，要比写进代码里更加灵活



### 断言（Predicates）

在上边的配置文件中，routes中的predicates就是一系列的断言，意思就是说，只有满足这样的条件就可以怎么怎么地。上边用到了Path这一个属性，除此之外，还有

![](https://gitee.com/lyn4ever/picgo-img/raw/master/img/20200413232234.png)

path是根据路径匹配，常用的还有Method(请求方法)、Host(请求的地址)等。这几个断言是“且”的关系，也就是说，如果你配置了多个predicates就要同时满足才能进行路由转发。详细参考

### 过滤器（Filter）

就是在web应用中使用的Filter，它有什么用呢？最常见的用处就是在前后端分离项目中，我们可以在网关层进行Token等授权的验证（明天给你们来个实例）。

## 

