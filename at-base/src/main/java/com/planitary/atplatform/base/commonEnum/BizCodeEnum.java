package com.planitary.atplatform.base.commonEnum;

import lombok.Getter;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.base.commonEnum
 * @name：BizCodeEnum
 * @Date：2023/12/21 11:19 下午
 * @Filename：BizCodeEnum
 * @description：
 * 业务编号枚举
 */
@Getter
public enum BizCodeEnum {

    /**
     * 文件来源的类型
     */
    UPLOAD_FILE("上传文件","1"),
    OPEN_LOCAL_FILE("本地文件","2"),

    /**
     * 异步调用方法
     */
    UPDATE_CURRENT_INTERFACE_INFO("更新当前接口信息","CM001"),
    EXTERNAL_INTERFACE_CALL("外部接口调用","CM002"),

    /**
     * 用例步骤执行方式
     */
    INTERFACE_CALL("接口调用","OT001"),
    DB_OPERATION("数据库操作","OT002"),
    REDIS_OPERATION("Redis操作","OT003"),
    RPC_INTERFACE_CALL("RPC接口调用","OT004"),


    /**
     * excel模板创建来源
     */
    BATCH_ADD_INTERFACE("BATCH_ADD_INTERFACE","EX001"),
    BATCH_ADD_TCS("BATCH_ADD_TCS","EX002"),
    BATCH_ADD_PROJECT("BATCH_ADD_PROJECT","EX003");

    private final String bizMsg;
    private final String bizCode;

    BizCodeEnum(String bizMsg,String bizCode){
        this.bizMsg = bizMsg;
        this.bizCode = bizCode;
    }
}
