package com.planitary.atplatform.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 接口执行封装DTO
 */
@Data
public class ExecuteDTO implements Serializable {

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 接口url
     */
    private String interfaceUrl;

    /**
     * 接口id
     */
    private String interfaceId;


    /**
     * 请求体
     */
    private Map<String,Object> requestBody;



    /**
     * 请求时间
     */
    private long requireTime;


}
