package com.planitary.atplatform.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：ExtractParamDTO
 * @Date：2024/1/17 9:21 下午
 * @Filename：ExtractParamDTO
 * @description：
 */
@Data
public class ExtractParamDTO {
    private String interfaceId;
    List<String> params;
}
