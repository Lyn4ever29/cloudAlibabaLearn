package cn.lyn4ever.goods.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信公众号 “小鱼与Java”
 *
 * @date 2020/4/13
 * @auther Lyn4ever
 */
@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("main")
    public String main() {
        return "来自用户模块的消息";
    }
}
