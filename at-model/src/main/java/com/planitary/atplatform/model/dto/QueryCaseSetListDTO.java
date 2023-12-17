package com.planitary.atplatform.model.dto;

import lombok.Data;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：QueryCaseSetListDTO
 * @Date：2023/12/17 9:51 下午
 * @Filename：QueryCaseSetListDTO
 * @description：
 */
@Data
public class QueryCaseSetListDTO {
    private String setName;
    private String interfaceIds;
    private String projectId;
}
