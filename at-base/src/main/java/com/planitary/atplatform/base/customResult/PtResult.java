package com.planitary.atplatform.base.customResult;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.base
 * @name：CustomeResult
 * @Date：2023/11/28 10:54 下午
 * @Filename：CustomeResult
 * @description：
 */

import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装自定义返回类
 */
@Data
@NoArgsConstructor
public class PtResult<T> {
    /**
     * 返回编码,默认0为成功的响应
     */
    private String code;

    /**
     * 错误信息
     */
    private String errMsg;

    /**
     * 主体数据
     */
    private T data;

    /**
     * 当前接口的traceId
     */
    private String traceId;

    /**
     * 动态绑定数据
     */
    private Map<Object,Object> map = new HashMap<>();

    // 成功调用的接口返回
    public static <T> PtResult<T> success(T object){
        PtResult<T> ptResult = new PtResult<>();
        ptResult.setData(object);
        ptResult.setCode("0");
        return ptResult;
    }

    // 错误调用的接口返回
    public static <T> PtResult<T> error(String message,String code){
        PtResult<T> ptResult = new PtResult<>();
        ptResult.data = null;
        ptResult.errMsg = message;
        ptResult.code = code;
        ptResult.traceId = MDC.get("traceId");
        return ptResult;
    }

}
