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
    EXTERNAL_INTERFACE_CALL("外部接口调用","CM002");

    private final String bizMsg;
    private final String bizCode;

    BizCodeEnum(String bizMsg,String bizCode){
        this.bizMsg = bizMsg;
        this.bizCode = bizCode;
    }
}
