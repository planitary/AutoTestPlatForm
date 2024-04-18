package com.planitary.atplatform.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.planitary.atplatform.model.po.ATPlatformInterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：InterfaceWithProjectDTO
 * @Date：2024/4/18 9:01 下午
 * @Filename：InterfaceWithProjectDTO
 * @description：
 */
@EqualsAndHashCode(callSuper = true)
@Data

public class InterfaceWithProjectDTO extends ATPlatformInterfaceInfo {
    @TableField("at_platform_project")
    private String projectName;
    private String projectUrl;
}
