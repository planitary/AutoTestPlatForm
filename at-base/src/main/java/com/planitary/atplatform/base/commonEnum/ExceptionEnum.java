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

    // 系统级通用异常
    SYSTEM_ERROR("系统异常","-0001"),
    UNKNOWN_ERROR("未知异常,请稍后","-1001"),
    PARAMETER_ERROR("参数错误","-1002"),
    OBJECT_NULL("对象为空","-1003"),

    // 分页相关异常
    PAGINATION_ERROR("分页异常","-2000"),
    PAGINATION_QUERY_ERROR("分页查询异常","-2001"),
    PAGINATION_PARAM_ERROR("分页参数异常","-2002"),
    PAGINATION_INTERNAL_ERROR("分页内部异常","-2003");

    private final String errMessage;
    private final String errCode;

    ExceptionEnum(String errorMessage,String code){
        this.errMessage = errorMessage;
        this.errCode = code;
    }
}
