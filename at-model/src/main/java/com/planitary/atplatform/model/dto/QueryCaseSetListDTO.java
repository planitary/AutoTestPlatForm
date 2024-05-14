package com.planitary.atplatform.model.dto;

import com.planitary.atplatform.model.po.PageParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：QueryCaseSetListDTO
 * @Date：2023/12/17 9:51 下午
 * @Filename：QueryCaseSetListDTO
 * @description：
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryCaseSetListDTO extends PageParams {
    private String setName;
    private List<String> interfaceIds;
    private String projectId;
}
