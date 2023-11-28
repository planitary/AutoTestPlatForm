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
@TableName("at_test_project")
public class ATTestProject {
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
     * 接口url
     */
    private String interfaceUrl;

    /**
     * 接口名称
     */
    private String interfaceName;

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

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private String updateUser;

}
