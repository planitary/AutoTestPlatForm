package com.planitary.atplatform.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExecuteResponseDTO {

    /**
     * 接口id
     */
    private String interfaceId;



    /**
     * 执行时间
     */
    private long executeTime;

    /**
     * 返回体
     */
    private String responseBody;

    /**
     * 响应时间
     */
    private String durationTime;
}
