package com.ly.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffineConfig {
    @Bean
   public Cache<String, Object> localCache(){
        return Caffeine.newBuilder().
                expireAfterWrite(10, TimeUnit.SECONDS).
                initialCapacity(20).maximumSize(20).recordStats().build();
    }
}
