package com.planitary.atplatform.model.po;

import lombok.Data;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.po
 * @name：ATTestInterfaceCallRecord
 * @Date：2023/12/5 9:19 下午
 * @Filename：ATTestInterfaceCallRecord
 * @description：
 */
@Data
public class ATTestInterfaceCallRecord {

    private Long id;
    /**
     * 记录id
     */
    private String recordId;

    /**
     * 接口id
     */
    private String interfaceId;

    /**
     * 执行时间
     */
    private Long executeTime;

    /**
     * 响应体
     */
    private String responseBody;

    /**
     * 接口响应时间
     */
    private Long durationTime;
    private String executorName;

    /**
     * 请求体
     */
    private String requestBody;
}
