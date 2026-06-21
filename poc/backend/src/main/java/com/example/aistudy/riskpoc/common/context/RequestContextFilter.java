package com.example.aistudy.riskpoc.common.context;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component("riskPocRequestContextFilter")
public class RequestContextFilter extends OncePerRequestFilter {
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestId = headerOrDefault(request, "X-Request-Id", nextRequestId());
        String operator = headerOrDefault(request, "X-Operator", "demo-operator");
        RequestContextHolder.set(new RequestContext(requestId, operator, System.nanoTime(), maskIp(request.getRemoteAddr())));
        response.setHeader("X-Request-Id", requestId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            RequestContextHolder.clear();
        }
    }

    private String headerOrDefault(HttpServletRequest request, String header, String defaultValue) {
        String value = request.getHeader(header);
        return StringUtils.hasText(value) ? value.trim() : defaultValue;
    }

    private String nextRequestId() {
        return "REQ-DEMO-AUTO-" + Long.toUnsignedString(RANDOM.nextLong(), 36).toUpperCase();
    }

    private String maskIp(String remoteAddr) {
        if (!StringUtils.hasText(remoteAddr)) {
            return "SAMPLE-IP-UNKNOWN";
        }
        return "SAMPLE-IP-" + remoteAddr.replaceAll("\\d+", "*");
    }
}
