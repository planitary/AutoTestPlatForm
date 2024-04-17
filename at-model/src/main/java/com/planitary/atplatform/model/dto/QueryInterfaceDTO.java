package com.planitary.atplatform.model.dto;

import com.planitary.atplatform.model.po.PageParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：QueryInterfaceDTO
 * @Date：2024/4/17 9:27 下午
 * @Filename：QueryInterfaceDTO
 * @description：
 */
@EqualsAndHashCode(callSuper = true)
@Data

public class QueryInterfaceDTO extends PageParams {

    private String interfaceUrl;
    private String interfaceName;
    private String projectId;
}
