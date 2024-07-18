package com.planitary.atplatform.base.utils;

import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.exception.ATPlatformException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.utils
 * @name：UniqueStringIdGenerator
 * @Date：2023/11/28 10:14 下午
 * @Filename：UniqueStringIdGenerator
 * @description： 全局通用id生成器
 */
@Slf4j
@Component
public class UniqueStringIdGenerator {
    private static final long EPOCH = 1627766400000L;
    private long lastTimestamp = -1L;
    private int sequence = 0;

    public synchronized String idGenerator() {
        long currentTimeStamp = System.currentTimeMillis();
        // 当前时间戳是否小于上一个时间戳，若是，则抛出异常（时钟回拨）
        if (currentTimeStamp < lastTimestamp) {
//            XueChengPlusException.exceptionCast("时钟回拨，无法生成id");
            log.error("时钟回拨，无法生成id!");
            ATPlatformException.exceptionCast(ExceptionEnum.UNKNOWN_ERROR);

        }
        // 当前时间戳等于上一个时间戳，说明为同一毫秒内生成的订单，此时对序列进行自增
        if (currentTimeStamp == lastTimestamp) {
            sequence = (sequence + 1) & 0xFFF;      // 使用12位序列号，0xFFF表示4095
            if (sequence == 0) {
                // 序列号用尽，等待下一毫秒
                currentTimeStamp = getNextTimeStamp();
            }
        } else {
            // 不在同一毫秒内，重置序列号（已进入下一时钟）
            sequence = 0;
        }
        lastTimestamp = currentTimeStamp;
        // 生成订单号:timestamp + sequence
        // 根据时间戳和序列号生成订单号。时间戳减去一个固定的起始时间（EPOCH）后左移12位，
        // 然后与序列号进行位或运算，得到最终的订单号。
        Long orderNo = ((currentTimeStamp - EPOCH) << 12) | sequence;
        log.info("当前时间戳:{},currentTimeStamp - EPOCH = {}", currentTimeStamp, currentTimeStamp - EPOCH);
        return String.valueOf(orderNo);
    }

    private Long getNextTimeStamp() {
        long currentTimeStamp = System.currentTimeMillis();
        while (currentTimeStamp <= lastTimestamp) {
            currentTimeStamp = System.currentTimeMillis();
        }
        return currentTimeStamp;
    }


    public static void main(String[] args) {
        UniqueStringIdGenerator uniqueStringIdGenerator = new UniqueStringIdGenerator();
        for (int i = 0; i < 6;i++) {
            System.out.println(uniqueStringIdGenerator.idGenerator());
        }
    }
}
