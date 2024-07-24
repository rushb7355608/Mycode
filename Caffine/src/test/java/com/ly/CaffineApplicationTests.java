package com.ly;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    @Test
    void test() throws InterruptedException {
        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(100)
                .build();

        // 往缓存里放一些数据
        cache.put("关键词1", "值1");
        cache.put("关键词2", "值2");
        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        System.out.printf(cache.get("关键词", s -> new Date().toString()));

    }


}

