package cn.lyn4ever.learn.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @auther 微信公众号 “小鱼与Java”
 * @date 2020/3/24
 */
@RestController
public class InfoController {

    @RequestMapping("link1")
    public String link1() {
        return UUID.randomUUID().toString();
    }


}
