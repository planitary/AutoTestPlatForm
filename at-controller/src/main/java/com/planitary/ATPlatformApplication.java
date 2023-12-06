package com.planitary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Author：planitary
 * @Package：com.planitary
 * @name：ATPlatformApplication
 * @Date：2023/11/29 11:05 下午
 * @Filename：ATPlatformApplication
 * @description：
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class ATPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(ATPlatformApplication.class, args);
    }

}
