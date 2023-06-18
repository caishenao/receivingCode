package cn.cai.receivingcode;

import cn.cai.receivingcode.redis.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ReceivingCodeApplication.class)
class ReceivingCodeApplicationTests {

    @Autowired
    private RedisCache redisCache;

    @Test
    void contextLoads() {
        redisCache.setCacheObject("key", "1111哎！");
        Object key = redisCache.getCacheObject("key");
        System.out.println(key);
    }

}
