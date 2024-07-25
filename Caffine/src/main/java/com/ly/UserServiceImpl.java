package com.ly;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;

import com.ly.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl {
    /**
     * 模拟数据库存储数据
     */
    private static HashMap<String, User> userMap = new HashMap<>();
    private final RedisTemplate<String, Object> redisTemplate;
    private final Cache<String, Object> caffeineCache;


    @Autowired
    public UserServiceImpl(RedisTemplate<String, Object> redisTemplate,
                           Cache<String, Object> localCache) {
        this.redisTemplate = redisTemplate;
        this.caffeineCache = localCache;
    }

    static {
        userMap.put("1", new User("1", "zhangsan"));
        userMap.put("2", new User("2", "lisi"));
        userMap.put("3", new User("3", "wangwu"));
        userMap.put("4", new User("4", "zhaoliu"));
    }


    public void add(User user) {
        // 1.保存Caffeine缓存
        caffeineCache.put(user.getId(), user);

        // 2.保存redis缓存
        redisTemplate.opsForValue().set(user.getId(), JSON.toJSONString(user), 20, TimeUnit.SECONDS);

        // 3.保存数据库(模拟)
        userMap.put(user.getId(), user);
    }


    public User getById(String id) {
        // 1.先从Caffeine缓存中读取
        Object o = caffeineCache.getIfPresent(id);
        if (Objects.nonNull(o)) {
            System.out.println("从Caffeine中查询到数据...");
            return (User) o;
        }

        // 2.如果缓存中不存在，则从Redis缓存中查找
        String jsonString = (String) redisTemplate.opsForValue().get(id);
        User user = JSON.parseObject(jsonString, User.class);
        if (Objects.nonNull(user)) {
            System.out.println("从Redis中查询到数据...");

            // 保存Caffeine缓存
            caffeineCache.put(user.getId(), user);
            return user;
        }

        // 3.如果Redis缓存中不存在，则从数据库中查询
        user = userMap.get(id);
        if (Objects.nonNull(user)) {
            System.out.println("从数据库中查询到数据...");

            // 保存Caffeine缓存
            caffeineCache.put(user.getId(), user);

            // 保存Redis缓存,20s后过期
            redisTemplate.opsForValue().set(user.getId(), JSON.toJSONString(user), 20, TimeUnit.SECONDS);
            return user;
        }
        System.out.println("从数据库中也没查询到数据...");
        return user;
    }

    /*
    Spring cache 两级缓存
     */
    @Caching(cacheable = {
            @Cacheable(value = "user",key = "#id",cacheManager = "caffeinecacheManager"),
            @Cacheable(value = "user",key = "#id",cacheManager = "rediscacheManager")
    })
    public User getById1(String id) {
        // 1.先从Caffeine缓存中读取
//        Object o = caffeineCache.getIfPresent(id);
//        if (Objects.nonNull(o)) {
//            System.out.println("从Caffeine中查询到数据...");
//            return (User) o;
//        }

        // 2.如果缓存中不存在，则从Redis缓存中查找
        System.out.println("进入方法了！");
//        String jsonString = (String) redisTemplate.opsForValue().get(id);
//        User user = JSON.parseObject(jsonString, User.class);
//        if (Objects.nonNull(user)) {
//            System.out.println("从Redis中查询到数据...");
//
//            // 保存Caffeine缓存
//            caffeineCache.put(user.getId(), user);
//            return user;
//        }

        // 3.如果Redis缓存中不存在，则从数据库中查询
        User user ;
        user=userMap.get(id);
        if (Objects.nonNull(user)) {
            System.out.println("从数据库中查询到数据...");

            // 保存Caffeine缓存
//            caffeineCache.put(user.getId(), user);

            // 保存Redis缓存,20s后过期
//            redisTemplate.opsForValue().set(user.getId(), JSON.toJSONString(user), 20, TimeUnit.SECONDS);
            return user;
        }
        System.out.println("从数据库中也没查询到数据...");
        return user;
    }


    public User update(User user) {
        User oldUser = userMap.get(user.getId());
        oldUser.setName(user.getName());
        // 1.更新数据库
        userMap.put(oldUser.getId(), oldUser);

        // 2.更新Caffeine缓存
        caffeineCache.put(oldUser.getId(), oldUser);

        // 3.更新Redis数据库
        redisTemplate.opsForValue().set(oldUser.getId(), JSON.toJSONString(oldUser), 20, TimeUnit.SECONDS);
        return oldUser;
    }


    public void deleteById(String id) {
        // 1.删除数据库
        userMap.remove(id);

        // 2.删除Caffeine缓存
        caffeineCache.invalidate(id);

        // 3.删除Redis缓存
        redisTemplate.delete(id);
    }

}