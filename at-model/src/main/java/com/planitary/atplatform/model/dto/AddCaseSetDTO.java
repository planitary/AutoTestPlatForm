package com.planitary.atplatform.model.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

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
    private String setId;
    private String setName;
    private String interfaceIds;
    private String remark;
    private Integer setWeight;
    private String parameterList;
    private String projectId;
    private String owner;
    private List<InterfaceInfoSIPDTO> interfaceInfoSIPDTOS;

    /** 各个接口提取的参数集合
     * {"extractParamDTOS":[{
     * "interfaceId":"234829423532",
     * "params":["$.store.id","$.store.token"]
     * },
     * {
     * "interfaceId":"324723894194",
     * "params":["$..id.token","$..id.pwd"]
     * },
     * {
     * "interfaceId":"37849242",
     * "params":[]
     * }]}
     */
    private List<ExtractParamDTO> extractParamDTOS;
}
