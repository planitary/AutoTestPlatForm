package com.planitary.atplatform.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.po
 * @name：ATTestInterfaceInfo
 * @Date：2023/12/1 10:29 下午
 * @Filename：ATTestInterfaceInfo
 * @description：
 */
@Data
@TableName("at_platform_interface_info")
public class ATPlatformInterfaceInfo {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 接口id
     */
    private String interfaceId;

    /**
     * 接口名
     */
    private String interfaceName;

    /**
     * 接口入参
     */
    private String requestBody;

    /**
     * 接口url
     */
    private String interfaceUrl;

    /**
     * 项目id，外键id
     */
    private String projectId;

    /**
     * 接口请求方式
     */
    private String methodType;

    private Integer version;

    @TableLogic
    private Integer isDelete;

    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    private String createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;
    private String updateUser;

    private String remark;



}
