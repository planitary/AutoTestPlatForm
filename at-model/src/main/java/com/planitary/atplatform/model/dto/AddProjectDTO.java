package com.planitary.atplatform.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：AddProjectDTO
 * @Date：2023/11/30 10:43 下午
 * @Filename：AddProjectDTO
 * @description：
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddProjectDTO extends BaseProjectDTO{
    private String projectName;
    private String projectUrl;
}
