package cn.cai.receivingCode;

import cn.cai.receivingCode.utils.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest(classes = ReceivingCodeApplication.class)
class ReceivingCodeApplicationTests {

    @Autowired
    private RedisCache redisCache;

    @Test
    void contextLoads() {
        System.out.println(redisCache);
//        redisCache.addSet("code", "11123");
//        System.out.println(redisCache.getSet("code"));
    }

}
