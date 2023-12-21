package com.planitary.atplatform.model.dto;

import lombok.Data;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：ExcelParseDTO
 * @Date：2023/12/21 10:21 下午
 * @Filename：ExcelParseDTO
 * @description：
 * Excel参数解析封装类
 */
@Data
public class ExcelParseDTO {
    private String interfaceUrl;
    private String requestBody;
    private String ownerName;
}
