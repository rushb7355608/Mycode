package com.ly;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Objects;

@SpringBootTest
class CaffineApplicationTests {
    @Resource
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        JSONObject o = (JSONObject) redisTemplate.opsForValue().get("user1");
        User user = o.toJavaObject(User.class);
        System.out.println(user);



    }


}

