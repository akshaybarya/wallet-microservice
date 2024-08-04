package com.akshaybarya.wallet.microservices.user_service.service;

import com.akshaybarya.wallet.microservices.user_service.data.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RedisService {
    @Autowired
    private ReactiveRedisTemplate<String, UserDetails> reactiveRedisTemplate;

    public Mono<Boolean> saveUser(UserDetails user) {
        return reactiveRedisTemplate.opsForValue().set(user.getUserId(), user);
    }

    public Mono<UserDetails> getUserById(String userId) {
        return reactiveRedisTemplate.opsForValue().get(userId);
    }
}
