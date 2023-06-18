package cn.cai.receivingcode.controller;

import cn.cai.receivingcode.commont.Result;
import cn.cai.receivingcode.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/activationCode")
public class ActivationCode {


    @Autowired
    private RedisCache redisCache;

    /**
     * 生成一个激活码，存储到redis中并返回
     */
//    @GetMapping(value = "/generate")
//    public Result<String> generate() {
//        // 1. 随机生成一个唯一的字符保证唯一
//
//        // 2. 存储进redis数据库中
//
//        // 3. 返回唯一的字符串
//
//
//    }


    @GetMapping("/getIP")
    public Result<Map<String, String>> getIP(HttpServletRequest request) {
        String header = request.getHeader("X-Forwarded-For");
        String remoteUser = request.getRemoteUser();
        Map<String,String> re = new HashMap<>();
        re.put("header", header);
        re.put("remoteUser", remoteUser);

        return Result.success(re);
    }
}
