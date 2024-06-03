package com.planitary.atplatform.model.dto;

import lombok.Data;

import java.security.SecureRandom;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：InterfaceInfoSIP
 * @Date：2024/5/29 8:28 下午
 * @Filename：InterfaceInfoSIP
 * @description：        接口信息简化版
 */
@Data
public class InterfaceInfoSIPDTO {
    private String interfaceId;
    private String interfaceName;
    private String interfaceUrl;
    private String remark;
    private String requestBody;
    private String interfaceStatus;
}
