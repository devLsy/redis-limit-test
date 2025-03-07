package com.test.lsy.redislimittest.limit.controller;

import com.test.lsy.redislimittest.limit.service.LimitService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LimitController {

    private final LimitService service;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, @RequestParam String clientIp) {
        // 로그인 시도
        return service.login(request, clientIp) ?
                "Login successful!"
                : "Too Many Requests - Please try again later.";
    }
}
