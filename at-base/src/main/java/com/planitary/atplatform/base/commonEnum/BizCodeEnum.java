package com.planitary.atplatform.base.commonEnum;

import com.planitary.atplatform.base.exception.ATPlatformException;
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
     * TCS步骤额外下拉框枚举
     */
    INTERFACE_RES_TIMEOUT("接口响应超时时间","InterfaceResTimeout"),
    SQL_EXE_TIMEOUT("Sql执行超时时间","SqlExeTimeout"),
    CONNECT_TIMEOUT("连接超时时间","ConnectTimeout"),
    WAIT_TIMEOUT("等待时间","WaitTimeout"),


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

    // 通过bizCode,获得bizMsg
    public static String getBizMsgByCode(String bizCode) throws IllegalAccessException {
        String bizValue = "";
        for (BizCodeEnum value: BizCodeEnum.values()){
            if (value.getBizCode().equals(bizCode)) {
                bizValue = value.getBizMsg();
                return bizValue;
            }
        }
        // 没有找到，抛出异常
        throw new IllegalAccessException("枚举不存在!");
    }
}
