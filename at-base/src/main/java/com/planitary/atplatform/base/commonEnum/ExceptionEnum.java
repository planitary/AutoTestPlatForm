package com.planitary.atplatform.base.commonEnum;

import lombok.Getter;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.base.commonEnum
 * @name：ExceptionEnum
 * @Date：2023/11/28 10:43 下午
 * @Filename：ExceptionEnum
 * @description：    常用错误枚举类
 */
@Getter
public enum ExceptionEnum {

    SYSTEM_ERROR("系统异常","-0001"),

    UNKNOWN_ERROR("未知异常,请稍后","-1001"),
    PARAMETER_ERROR("参数错误","-1002"),
    OBJECT_NULL("对象为空","-1003");

    private final String errMessage;
    private final String errCode;

    ExceptionEnum(String errorMessage,String code){
        this.errMessage = errorMessage;
        this.errCode = code;
    }
}
