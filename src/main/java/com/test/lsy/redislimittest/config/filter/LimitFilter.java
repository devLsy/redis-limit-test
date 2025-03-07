package com.test.lsy.redislimittest.config.filter;

import com.test.lsy.redislimittest.limit.service.LimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

//@Component
@RequiredArgsConstructor
@Slf4j
public class LimitFilter extends GenericFilterBean {

    private final LimitService service;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String clientIp = httpRequest.getRemoteAddr();

        log.info("clientId => {} request", clientIp);

//        if(!service.isAllowed(clientIp)) {
//            HttpServletResponse httpResponse = (HttpServletResponse) response;
//            httpResponse.setStatus(429);
//            httpResponse.getWriter().write("Too Many Requests!!!!!");
//            return;
//        }

        // 로그인 요청인지 확인 (POST 요청인 경우)
        if ("/api/login".equals(httpRequest.getRequestURI()) && "POST".equals(httpRequest.getMethod())) {
            // 로그인 시도, IP와 요청을 LimitService에 전달
            boolean isSuccess = service.login(httpRequest, clientIp);

            // 로그인 실패 시 Too Many Requests 상태 반환
            if (!isSuccess) {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(429);  // HTTP 429 Too Many Requests
                httpResponse.getWriter().write("Too Many Requests - Please try again later.");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
