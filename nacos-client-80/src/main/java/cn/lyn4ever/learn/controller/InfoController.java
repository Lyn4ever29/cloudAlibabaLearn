package cn.lyn4ever.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * @auther 微信公众号 “小鱼与Java”
 * @date 2020/3/24
 */
@RestController
public class InfoController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("link1")
    public String link1() {

        return restTemplate.getForObject("http://nacos-server-8001/link1",String.class);
    }

}
