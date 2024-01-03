package com.planitary.atplatform.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.handler
 * @name：AsyncTraceIdHandler
 * @Date：2024/1/3 10:48 下午
 * @Filename：AsyncTraceIdHandler
 * @description：
 */
@Slf4j
public class AsyncTraceIdHandler implements Runnable {


    @Override
    public void run() {
        String traceId = UUID.randomUUID().toString();

        try {
            // 在MDC中设置traceId
            MDC.put("traceId", traceId);

            // 执行异步任务，打印日志

            // 其他异步操作...

        } finally {
            // 清除MDC
            MDC.remove("traceId");
        }
    }
}
