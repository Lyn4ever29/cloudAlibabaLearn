# 使用GateWay进行网关鉴权

GateWay作为网关路由是挡在了我们的系统最前边，进行动态路由配置和转发。那么我们就可以在网关层进行网关鉴权。

https://cloud.spring.io/spring-cloud-gateway/2.2.x/reference/html/

## Gateway过滤器的介绍

之前说过GateWay的组件中有Filter(过滤器)这一功能，就是web开发的三大组件（Servlet、Filter、Listener）中的Filter，但是Gateway中使用的是WebFlux，而不是Servlet，有兴趣的可以了解下。在GateWay中有很多内置的过滤器，而且我们还可以自定义一个过滤器。

## Gateway内置过滤器

####	生命周期

- PRE： 这种过滤器在请求被路由之前调用。
- POST：这种过滤器在路由到微服务以后执行。

#### 类型

* GatewayFilter 和Predicate一样，用在单个路由上

* GlobalFilter 用在整个网关之前

## 自定义过滤器

自定义一个类实现这两个类就以了，直接上代码：

我们的需求就是，只有当你的请求参数中的username=admin才给你放行。

```java
/**
 * 微信公众号 “小鱼与Java”
 * <p>
 * 自定义一个GlobalFilter类
 *
 * @date 2020/4/14
 * @auther Lyn4ever
 */
@Component //一定要让Spring管理这个bean
public class CustomFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        /*
        这个request对象可以获取更多的内容
       比如，如果是使用token验证的话，就可以判断它的Header中的Token值了
       为了演示方便，我就判断了它的参数
         */
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String username = queryParams.getFirst("username");
        if (!username.equals("admin")) {

            //不允许访问，禁止访问
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE); //这个状态码是406

            return exchange.getResponse().setComplete();
        }
        //放行
        return chain.filter(exchange);
    }

    /**
     * 这是Ordered接口的中的方法
     * 过滤器有一个优先级的问题，这个值越小，优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
```

> 具体的演示就不做了，自行下载代码就可以

### GateWay集成SpringSecurity

> 请自行具备一点儿SpringSecurity使用能力，如果不会的话，关注“小鱼与Java”后台回复SpringSecurity获取教程

> Gateway用的是webflux，不同于Servlet，所以使用上会和在SpringMVC中使用有很大差别。[官网地址](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-web-security)

* 引入依赖

```xml
<!--springsecurity依赖-->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

引入这个依赖后，和我们使用SpringMVC时是一样的，输入任何地址后，都会重定向到login

![](https://gitee.com/lyn4ever/picgo-img/raw/master/img/20200414234017.png)

用户名：```user```

密码：启动应用时，在控制台上有打印出来的。

* 配置类(主要是注入这几个bean)

```java
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * 用户的接口类
     * @return
     */
    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
	      //自定义一个用户
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    /**
     * 主要过滤配置类
     * @param http
     * @return
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers("/order/**").permitAll()
                .pathMatchers("/user/**").hasRole("ADMIN")
                .anyExchange().authenticated()
                .and()
                .httpBasic().and()
                .formLogin();
        return http.build();
    }
}
```

* 在Gateway中使用SpringSecurity其实就是在Webflux中使用SpringSeccurity。因为Gateway本就是基于Spring5的响应式编程，在性能上要比SpringMVC(传统的Servlet)开发好很多。

代码地址：关注微信公众号“小鱼与Java”,回复SpringCloud获取

![](https://lyn4ever.gitee.io/img/wx/gzh2.png)