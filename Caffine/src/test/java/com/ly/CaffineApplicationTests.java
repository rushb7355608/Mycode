package com.ly;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ly.pojo.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class CaffineApplicationTests {
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    CacheManager caffeinecacheManager;

    @Test
    void contextLoads() {
        JSONObject o = (JSONObject) redisTemplate.opsForValue().get("user1");
        User user = o.toJavaObject(User.class);
        System.out.println(user);


    }

    @Resource
    UserServiceImpl userService;

    @Test
    void test() throws InterruptedException {
        try {
            if (Objects.nonNull(caffeinecacheManager.getCache("user").get("1").get()))
                System.out.println(caffeinecacheManager.getCache("user").get("1").get());
        } catch (Exception e) {

        }


        User user = userService.getById1("1");
        System.out.println(user);
        Thread.sleep(1000);
        try {
            if (Objects.nonNull(caffeinecacheManager.getCache("user").get("1").get()))
                System.out.println(caffeinecacheManager.getCache("user").get("1").get());
        } catch (Exception e) {

        }
        user = userService.getById1("1");
        System.out.println(user);


    }


}

