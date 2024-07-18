package com.planitary.atplatform.base.utils;

import java.util.Base64;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.base.utils
 * @name：GeneralIdGenerator
 * @Date：2023/12/1 10:56 下午
 * @Filename：GeneralIdGenerator
 * @description：        通用id生成器
 */
public class GeneralIdGenerator {
    private static final Random random = new Random();
    public static String generateId(){
        long timeStamp = System.currentTimeMillis() % 10000000;
        int randomPart = random.nextInt(9999);
        return timeStamp + String.format("%03d",randomPart);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 8; i++ ){
            System.out.println(generateId());
        }
    }
}
