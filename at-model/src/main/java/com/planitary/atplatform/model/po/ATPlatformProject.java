package com.planitary.atplatform.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.po
 * @name：ATTestProject
 * @Date：2023/11/28 9:44 下午
 * @Filename：ATTestProject
 * @description：  test_project实体类
 */
@Data
@TableName("at_platform_project")
public class ATPlatformProject {
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 项目前缀url
     */
    private String projectUrl;

    /**
     * 项目id
     */

    private String projectId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标记位
     */
    @TableLogic
    private int isDelete;

    /**
     * 项目所属人
     */
    private String projectOwner;

    /**
     * 项目所属组
     */
    private String projectGroup;

    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    private String createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

    private String updateUser;

}
