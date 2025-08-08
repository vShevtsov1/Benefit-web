package com.example.Redi.users.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> void saveToRedis(String key, T object, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, object, timeout, unit);
    }

    public <T> T getFromRedis(String key, Class<T> clazz) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    public void deleteFromRedis(String key) {
        redisTemplate.delete(key);
    }
}
