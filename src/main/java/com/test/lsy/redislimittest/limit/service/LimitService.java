package com.test.lsy.redislimittest.limit.service;

import jakarta.servlet.http.HttpServletRequest;
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
    private static final int LIMIT = 30;           // 최대 요청 횟수
    private static final int WINDOW_SECONDS = 120; // 제한 시간 (초)

    public boolean isAllowed(String clientIp) {
        String key = "rate_limit:" + clientIp;
        Long currentCount = redisTemplate.opsForValue().increment(key);

        log.info("isAllowed Request~~~~");

        if (currentCount != null && currentCount == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(WINDOW_SECONDS));
        }
        return currentCount != null && currentCount <= LIMIT;
    }

    public boolean login(HttpServletRequest request, String clientIp) {
        if (!isAllowed(clientIp)) {
            return false;  // 제한을 초과한 경우 로그인 실패
        }
        request.getSession().setAttribute("isLogin", "1");
        return true;
    }
}
