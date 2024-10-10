package com.planitary.atplatform.base.exception;

import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import lombok.Getter;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.base.exception
 * @name：ATPlatformException
 * @Date：2023/11/28 10:39 下午
 * @Filename：ATPlatformException
 * @description：自定义异常类
 */
@Getter
public class ATPlatformException extends RuntimeException {
    private String errCode;

    public ATPlatformException(){}

    public ATPlatformException(String message){
        super(message);
    }
    public ATPlatformException(String message,String code){
        super(message);
        this.errCode = code;
    }

    // 父类runtime异常
    public static void exceptionCast(String message){
        throw new RuntimeException(message);
    }

    // 自定义异常参数
    public static void exceptionCast(String message,String code){
        throw new ATPlatformException(message,code);
    }

    // 支持自定义的错误参数
    public static void exceptionCast(ExceptionEnum exceptionEnum,String code){
        throw new ATPlatformException(exceptionEnum.getErrMessage(),code);
    }

    // 系统支持的错误参数
    public static void exceptionCast(ExceptionEnum exceptionEnum){
        throw new ATPlatformException(exceptionEnum.getErrMessage(),exceptionEnum.getErrCode());
    }

}
