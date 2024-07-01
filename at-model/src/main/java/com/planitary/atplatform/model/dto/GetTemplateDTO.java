package com.planitary.atplatform.model.dto;

import lombok.Data;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：getTemplateDTO
 * @Date：2024/7/1 10:45 下午
 * @Filename：getTemplateDTO
 * @description：        Excel模板下载通用DTO
 */
@Data
public class GetTemplateDTO {
    /**
     * 通用业务标识Code
     */
    private String bizCode;

    /**
     * 通用业务标识名
     */
    private String bizName;

    /**
     * 冗余——excel版本
     */
    private String excelVersion;
}
