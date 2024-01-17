package com.planitary.atplatform.model.po;

import com.baomidou.mybatisplus.annotation.*;
import com.planitary.atplatform.model.dto.ExcelParseDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.po
 * @name：ATPlatformTCSExtractParam
 * @Date：2024/1/17 9:59 下午
 * @Filename：ATPlatformTCSExtractParam
 * @description：
 */
@Data
@TableName("at_platform_tcs_extract_param")
public class ATPlatformTCSExtractParam {

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 业务id
     */
    private String bizId;

    private String interfaceId;
    /**
     * 提取参数列表
     */
    private String extractParams;

    /**
     * 集合id
     */
    private String caseSetId;

    /**
     * 维护人
     */
    private String owner;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
