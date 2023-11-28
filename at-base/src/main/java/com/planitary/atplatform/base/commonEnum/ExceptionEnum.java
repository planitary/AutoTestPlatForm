package com.planitary.atplatform.base.commonEnum;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.base.commonEnum
 * @name：ExceptionEnum
 * @Date：2023/11/28 10:43 下午
 * @Filename：ExceptionEnum
 * @description：    常用错误枚举类
 */
public enum ExceptionEnum {

    UNKNOWN_ERROR("系统异常,请稍后","-1001"),
    PARAMETER_ERROR("参数错误","-1002"),
    OBJECT_NULL("对象为空","-1003");

    private final String errMessage;
    private final String errCode;

    public String getErrMessage(){
        return this.errMessage;
    }
    public String getErrCode(){
        return errCode;
    }
    ExceptionEnum(String errorMessage,String code){
        this.errMessage = errorMessage;
        this.errCode = code;
    }
}
