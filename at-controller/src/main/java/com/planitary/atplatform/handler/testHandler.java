package com.planitary.atplatform.handler;

import com.planitary.atplatform.base.customResult.PtResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.handler
 * @name：testHandler
 * @Date：2023/12/13 10:28 下午
 * @Filename：testHandler
 * @description：
 */
@RestController
public class testHandler {

    @PostMapping("/test")
    public PtResult<String> testController(@RequestBody Map<String,Object> param){
        return PtResult.success(param.get("ocCode") + "+" + "ocName");
    }

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
