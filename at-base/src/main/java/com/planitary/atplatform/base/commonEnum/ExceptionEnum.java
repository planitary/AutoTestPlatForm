package com.planitary.atplatform.base.commonEnum;

import lombok.Getter;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.base.commonEnum
 * @name：ExceptionEnum
 * @Date：2023/11/28 10:43 下午
 * @Filename：ExceptionEnum
 * @description：常用错误枚举类
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
    PAGINATION_INTERNAL_ERROR("分页内部异常","-2003"),

    // db相关异常
    QUERY_ERROR("查询异常","-3000"),
    RESULT_IS_NONE("查询结果为空","-3001"),
    INSERT_FAILED("插入失败","-3002"),
    UPDATE_FAILED("更新失败","-3003"),
    DELETE_FAILED("删除失败","-3004"),
    CONNECTION_FAILED("数据库链接异常","-3009"),

    // 文件读取相关
    FILE_LOAD_FAILED("文件读取失败","-4000"),
    FILE_NOT_EXIST("文件不存在","-4001"),
    PERMISSION_DENIED("无权访问","-4002"),
    SUFFIX_NAME_IS_INVALID("后缀名非法","-4003"),
    PARSE_FAILED("解析失败","-4004"),

    // 接口调用相关
    CALL_FAILED("接口调用失败","-8001"),
    CALL_BACK_FAILED("获取接口回调失败","-8002"),

    // 业务通用异常
    BIZ_ERROR("业务异常","-9009");

    private final String errMessage;
    private final String errCode;

    ExceptionEnum(String errorMessage,String code){
        this.errMessage = errorMessage;
        this.errCode = code;
    }
}
