package com.planitary.atplatform.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.po
 * @name：ATPlatformTCSInterface
 * @Date：2024/5/28 9:42 下午
 * @Filename：ATPlatformTCSInterface
 * @description：
 */
@Data
public class ATPlatformTCSInterface {
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 集合id
     */
    private String setId;

    /**
     * 接口id
     */
    private String interfaceId;

    private String createUser;

    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    @TableLogic
    private Integer isDeleted;

    private Integer interfaceStatus;

    private String setName;

}
