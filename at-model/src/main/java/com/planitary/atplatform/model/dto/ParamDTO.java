package com.planitary.atplatform.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：ParamDTO
 * @Date：2023/12/22 11:37 下午
 * @Filename：ParamDTO
 * @description：
 * 封装接口参数
 */
@Data
public class ParamDTO {
    private Map<String,?> paramsMap;
}
