package cn.lyn4ever.learn.nacosserver8002.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther 微信公众号 “小鱼与Java”
 * @date 2020/3/24
 */
@RestController
@RefreshScope //这是一个SpingCloud的原生注解，可以实现配置的动态刷新
public class InfoController {

    /*
    这个中的 : 的意思就是说，如果配置文件中没有，就直接使用后边的那个字符串，我写的是nothing
    在本地项目中，我们并没有配置configInfo,
    所以如果这时候调用http://localhost:8002/info
    返回就是nothing
     */
    @Value("${configInfo:nothing}")
    private String configInfo;

    /**
     * 用做配置中心的演示
     *
     * @return
     */
    @GetMapping("info")
    public String getConfig() {
        return "当前配置的内容是："+configInfo;
    }

}
