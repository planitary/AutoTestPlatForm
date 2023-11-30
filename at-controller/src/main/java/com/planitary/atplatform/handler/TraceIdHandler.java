package com.planitary.atplatform.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.config.Order;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.handler
 * @name：TraceIdHandler
 * @Date：2023/11/30 9:46 下午
 * @Filename：TraceIdHandler
 * @description：
 */
@Slf4j
public class TraceIdHandler implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 生成唯一的 Trace ID
        String traceId = UUID.randomUUID().toString();

        // 将 Trace ID 放入 MDC
        MDC.put("traceId", traceId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求完成后清除 Trace ID
        MDC.remove("traceId");
    }

}
