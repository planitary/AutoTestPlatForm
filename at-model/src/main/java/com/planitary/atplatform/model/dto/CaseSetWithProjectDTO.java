package com.planitary.atplatform.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.planitary.atplatform.model.po.ATPlatformCaseSet;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：CaseSetWithProjectDTO
 * @Date：2024/5/15 9:25 下午
 * @Filename：CaseSetWithProjectDTO
 * @description：
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CaseSetWithProjectDTO extends ATPlatformCaseSet {
    @TableField("at_platform_project")
    private String projectName;
    private String projectUrl;
}
