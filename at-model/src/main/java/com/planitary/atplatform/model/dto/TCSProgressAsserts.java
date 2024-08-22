package com.planitary.atplatform.model.dto;

import lombok.Data;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：TCSProgressAsserts
 * @Date：2024/8/22 9:47 下午
 * @Filename：TCSProgressAsserts
 * @description：
 */
@Data
// 步骤中的断言
public class TCSProgressAsserts {
    private String assertType;
    private String param;
    private String value;
}
