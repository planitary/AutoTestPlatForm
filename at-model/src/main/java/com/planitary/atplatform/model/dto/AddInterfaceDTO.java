package com.planitary.atplatform.model.dto;

import lombok.Data;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：AddInterfaceDTO
 * @Date：2023/12/5 9:48 下午
 * @Filename：AddInterfaceDTO
 * @description：
 */
@Data
public class AddInterfaceDTO {

    private String interfaceName;
    private String interfaceUrl;
    private String projectId;

}
