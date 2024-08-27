package com.planitary.atplatform.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：AddTCSProgressDTO
 * @Date：2024/8/22 9:44 下午
 * @Filename：AddTCSProgressDTO
 * @description：
 */
// TCS添加步骤
@EqualsAndHashCode(callSuper = true)
@Data
public class AddTCSProgressInfoDTO extends BaseInterfaceDTO {
    private String stepName;

    private String DBContent;
    private String operationType;
    private String requestBody;
    private String remark;
    private List<TCSProgressAsserts> asserts;
    private String key;
    private String children;
    private String extraType;
    private String extraValue;
    private String value;

}
