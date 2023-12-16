package com.planitary.atplatform.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：AddCaseSetDTO
 * @Date：2023/12/14 10:33 下午
 * @Filename：AddCaseSetDTO
 * @description：
 */
@Data
public class AddCaseSetDTO {
    private String setName;
    private String interfaceIds;
    private String remark;
    private Integer setWeight;
    private String parameterList;
    private String projectId;

}
