package com.planitary.atplatform.model.dto;

import lombok.Data;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：AddProjectDTO
 * @Date：2023/11/30 10:43 下午
 * @Filename：AddProjectDTO
 * @description：
 */
@Data
public class AddProjectDTO {
    private String projectName;
    private String interfaceUrl;
    private String interfaceName;
}
