package com.planitary.atplatform.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 接口执行封装DTO
 */
@Data
public class ExecuteDTO {

    /**
     * 接口id
     */
    private String interfaceId;

    /**
     * 接口url
     */
    private String interfaceUrl;

    /**
     * 请求体
     */
    private Map<String,String> requestBody;

    /**
     * 执行时间
     */
    private LocalDateTime executeTime;

    /**
     * 请求时间
     */
    private LocalDateTime requireTime;

    /**
     * 返回体
     */
    private String responseBody;

    /**
     * 响应时间
     */
    private String durationTime;
}
