package com.planitary.atplatform.model.dto;

import com.planitary.atplatform.model.po.ATPlatformInterfaceInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：ParamDTO
 * @Date：2023/12/22 11:37 下午
 * @Filename：ParamDTO
 * @description：
 * 请求参数，最内部json
 */
@Data
public class ParamDTO implements Serializable {
    private Map<String,Object> params;
}
