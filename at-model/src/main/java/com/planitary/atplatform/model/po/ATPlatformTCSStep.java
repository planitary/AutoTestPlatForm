package com.planitary.atplatform.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.po
 * @name：ATPlatformTCSStep
 * @Date：2024/8/20 9:28 下午
 * @Filename：ATPlatformTCSStep
 * @description：
 */
@Data
@TableName("at_platform_test_case_set_step")
public class ATPlatformTCSStep {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 步骤id
     */
    private String stepId;

    /**
     * 步骤名
     */
    private String stepName;

    /**
     * 用例id
     */
    private String caseId;

    /**
     * 操作类型
     */
    private String operationType;

    private String remark;

    private String interfaceId;

    /**
     * 数据库语句
     */
    private String dbContent;
    /**
     * 步骤序位
     */
    private Integer stepSeq;

    /**
     * 断言json
     */
    private String asserts;

    /**
     * 额外配置类型
     */
    private String extraType;

    /**
     * 额外配置值
     */
    private String extraValue;

    /**
     * 步骤业务id（外部使用）
     */
    private String businessKey;

    /**
     * 额外配置描述
     */
    private String extraDesc;

    private Integer isDelete;
    private String createUser;
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;
    private String updateTime;

}
