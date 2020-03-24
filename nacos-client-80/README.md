# 我觉得Nacos用起来还不错

在使用SpringCloud做分布式微服务架构时，注册中心是必不可少的一个组件。目前可以用的主要有：Eureka、Consul、Zookeeper。今天，我们就来说一下Alibaba的Nacos怎么样？

## 下载与安装

* 下载地址[https://github.com/alibaba/nacos/releases](https://github.com/alibaba/nacos/releases)

* 安装：

  * Windows 下载解压后（.zip)，直接点击bin/start.bat就可以了。
  * Linux下载解压后(.tar.gz)，同样，也是运行 bin/start.sh 脚本。

* 打开控制台：

  Nacos提供了一个可视化的操作平台，安装好之后，在浏览器中输入```http://localhost:8848```就可以访问了,默认的用户名和密码都是nacos(我使用的1.2.0版本。默认将密码验证给关了，这个选项后边会说)

  ![Nacos注册中心](https://gitee.com/lyn4ever/picgo-img/raw/master/img/20200324225122.png)

## Nacos简介

Nacos是由阿里巴巴开源的一个好东西，直接贴上它的官网，就不再这么累赘了，毕竟是国人开发的，是有中文文档的，上边写的很全面。[https://nacos.io/zh-cn/docs/quick-start.html](https://nacos.io/zh-cn/docs/quick-start.html)

##	Nacos作为注册中心

​		它和Eureka不一样，并不需要创建新的web项目，而是和Zookeeper和Consul一样，只需要下载安装启动后，将我们的微服务注册进去就可以了。

​		创建两个微服务，一个客户端（调用者）和一个服务端（提供者），

* 引入依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

* 在它们的配置文件中引入如下代码：

```yaml
spring:
  cloud:
    nacos:
      discovery:
      # Nacos的地址
        server-addr: localhost:8848
```

* 主启动类上添加(不管是哪种注册中心，这个一定要有)：

```java
@EnableDiscoveryClient
```

* 当然，在客户端还是要添加ribbon的负载均衡的，但是不用额外添加依赖，nacos已经添加了

```java
@Configuration
public class AppConfig {
    
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```

* 好了，打开Nacos的控制台，然后就可以看到这两个微服务了。

![Nacos注册中心](https://gitee.com/lyn4ever/picgo-img/raw/master/img/20200324224903.png)

## Nacos作为分布式配置中心

之前我们是使用SpringCloudConfig从github等仓库上拉取的配置文件，但是用了Nacos后，我们就可以从Nacos中直接配置了，是不是很方便啊。新建了一个项目，```nacos-config-server-8002```

#### pom.xml引入下边依赖

```xml
<!--分布式配置中心-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

#### application.yml中加入如下配置内容

```YAML
spring:
  profiles:
    active: dev
```

#### 添加一个bootstrap.yml

```yaml
server:
  port: 8002
spring:
  application:
    name: nacos-config-server-8002
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848
#       配置文件类型，有yaml和properties ,注意一定是yaml,不是yml
        file-extension: yaml
```

> 1. application.yml中的配置是什么意思？



> 2. 为什么要引入bootstrap.yml?

​	简单来说，这个配置是和application.yml是一样的，不这它要比appliation.yml先加载

#### Controller中的接口

```java
@RestController
@RefreshScope //这是一个SpingCloud的原生注解，可以实现配置的动态刷新
public class InfoController {
    /*
    这个中的 : 的意思就是说，如果配置文件中没有，就直接使用后边的那个字符串，我写的是nothing
    在本地项目中，我们并没有配置configInfo,
    所以如果这时候调用http://localhost:8002/info
    返回就是nothing
     */
    @Value("${configInfo}")
    private String configInfo;

    /**
     * 用做配置中心的演示
     *
     * @return
     */
    @GetMapping("info")
    public String getConfig() {
        return configInfo;
    }
}
```



#### Nacos中的几个空间概念：

* NameSpaces（命名空间）

  相当于我们一个项目中的包名，Nacos中可以新建多个命名空间。微服务注册的时候，可以通过配置```spring.cloud.nacos.discovery.namespace```,当然，配置文件也可以配置namespace来指定对应的名称空间，如果不配置就是使用默认的public 空间。

  假如，你是好几个项目共用一个Nacos集群，就可以通过namespce来区分项目。

  在Nacos的控制台的最下边有一个命名空间，你可以新建一个试，建完再次点击服务列表和配置列表上边会出现你的命令空间选择（如下图，我新建了一个space1的空间，public是默认的）


![Nacos注册中心](https://gitee.com/lyn4ever/picgo-img/raw/master/img/20200324232147.png)

  * Group（分组）

    相当于Java中的类名，同样，一个包下可以有多个类。不过这个只是相对于配置文件来说，对于服务注册没有这么一说。它有一个默认的分组就是DEFAULT_GROUP，在新建配置文件时就会有(如下图)

* DataID

  这个就相当于类中的方法，同样，一个类中就会有多个方法名。

![Nacos注册中心](https://gitee.com/lyn4ever/picgo-img/raw/master/img/20200324232340.png)

​		我们的配置文件就是在上图中去添加的，下边是我截取了Nacos官方文档中对于DataID的说明：

![Nacos注册中心](https://gitee.com/lyn4ever/picgo-img/raw/master/img/20200324231217.png)

#### 新建一个配置文件试试

​	在配置列表中新建一个配置文件，如果你有多个命名空间，注意选择你的项目中连接的那个

![Nacos注册中心](https://gitee.com/lyn4ever/picgo-img/raw/master/img/20200324233920.png)

只要点了右下角的发布，就会自动配置了，再次访问```http://localhost:8002/info```，就会看到已经更改了，这个是实时更改的。

这些都是很简单的操作，只要你自己做一次，就一定会使用了

##	Nacos的配置文件持久化

Nacos使用的是嵌入式数据库Derby，有关嵌入式数据库，可以参考[在Spring中使用嵌入式数据库-H2](https://blog.csdn.net/weixin_43605736/article/details/104177910)，虽然数据库不同，但是原理操作方法一致。但是，我们想换成我们的mysql用来存储nacos的数据，可否？

* 导入数据到你本地的Mysql库

  在Nacos安装包下的conf目录下有个nacos-mysql.sql文件，放到你的Mysql工具中执行一遍（它这个sql语句中建库，先建个库名叫nacos_config）

* 修改conf下的application.properties文件

```prop
### 98行左右，这个设置为true就是开启nacos启动的登录验证，默认用户名和密码就是nacos
nacos.core.auth.enabled=true

### 在最后添加如下：
spring.datasource.platform=mysql

db.num=1
db.url.0=jdbc:mysql://127.0.0.1:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=root
db.password=123
```

> ​	以上这些内容全都来自于Nacos官网

![Nacos注册中心](https://gitee.com/lyn4ever/picgo-img/raw/master/img/20200325001122.png)

这样，再次启动后Nacos后，你之前的配置文件就全都不见了，因为你配置了Mysql库。这样，你每次修改后，就会进Mysql库，这个库很简单的，你大概看一下就能明白了。

微信关注”小鱼与Java“，回复”SpingCloud“获取更多SpringCloud学习资料

![Nacos](https://lyn4ever.gitee.io/img/wx/gzh2.png)