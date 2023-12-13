package com.planitary.atplatform.handler;

import java.time.Duration;
import java.time.Instant;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.handler
 * @name：testHandler
 * @Date：2023/12/13 10:28 下午
 * @Filename：testHandler
 * @description：
 */
public class testHandler {
    public static void main(String[] args) {
        // 记录接口调用开始时间
        long l = System.currentTimeMillis();
        // 模拟一个接口调用
        performAPICall();

        // 记录接口调用结束时间
        long s = System.currentTimeMillis();

        // 计算接口调用耗时
        System.out.println(l);
//        System.out.println("API call took: " + duration.toMillis() + " milliseconds");
    }

    private static void performAPICall() {
        // 模拟接口调用的业务逻辑
        try {
            Thread.sleep(2000); // 模拟接口调用耗时2秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
