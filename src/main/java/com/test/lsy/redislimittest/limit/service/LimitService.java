package com.test.lsy.redislimittest.limit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class LimitService {

    private final StringRedisTemplate redisTemplate;
    private static final int LIMIT = 10;           // 최대 요청 횟수
    private static final int WINDOW_SECONDS = 120; // 제한 시간 (초)

    public boolean isAllowed(String clientIp) {
        String key = "rate_limit:" + clientIp;
        Long currentCount = redisTemplate.opsForValue().increment(key);

        if (currentCount != null && currentCount == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(WINDOW_SECONDS));
        }
        return currentCount != null && currentCount <= LIMIT;
    }
}
