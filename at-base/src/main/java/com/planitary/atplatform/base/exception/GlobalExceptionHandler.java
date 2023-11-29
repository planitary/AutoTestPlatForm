package com.planitary.atplatform.base.exception;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.base.exception
 * @name：GlobalExceptionHandler
 * @Date：2023/11/28 10:36 下午
 * @Filename：GlobalExceptionHandler
 * @description：    全局异常处理器
 */

import com.planitary.atplatform.base.customResult.PtResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 处理自定义的的异常（此类异常为可预知异常）
    @ExceptionHandler(ATPlatformException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)       // 抛出异常后状态码返回500
    public PtResult<Object> doXueChengPlusException(ATPlatformException e){
        log.error("捕获异常:{}",e.getMessage());
        String traceId = MDC.get("traceId");
        String errMessage = e.getMessage();
        String errCode = e.getErrCode();
        return PtResult.error(errMessage,errCode,traceId);
    }




}
