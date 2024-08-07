package com.planitary.atplatform.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.po
 * @name：ATPlatformCaseSet
 * @Date：2023/12/14 9:49 下午
 * @Filename：ATPlatformCaseSet
 * @description：
 */
@Data
@TableName("at_platform_test_case_set")
public class ATPlatformCaseSet implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 集合id
     */
    private String setId;

    /**
     * 集合名称
     */
    private String setName;

    /**
     * 接口id列表
     */
    private String interfaceIds;

    /**
     * 项目列表
     */
    private String projectId;

    /**
     * 集合权重
     */
    private Integer setWeight;

    /**
     * 步骤数
     */
    private Integer steps;

    /**
     * 提取参数的列表
     */
    private String parameterList;

    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    private String createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;
    private String updateUser;

    @TableLogic
    private Integer isDelete;

    private String remark;

    private String owner;
}
