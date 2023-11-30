package com.planitary.atplatform.config;

import com.planitary.atplatform.handler.TraceIdHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.config
 * @name：WebConfig
 * @Date：2023/11/30 10:12 下午
 * @Filename：WebConfig
 * @description：
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TraceIdHandler()).addPathPatterns("/**");
    }
}
