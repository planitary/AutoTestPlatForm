package com.planitary.atplatform.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：ChosenParamDTO
 * @Date：2023/12/22 11:00 下午
 * @Filename：ChosenParamDTO
 * @description： 封装接口参数列表（带接口url）
 * 一个典型的DTO格式为：
 * <p>
 * {
 * 	"interfaceUrl": "cornerstone/test",
 * 	"interfaceDTOS": [
 *                {
 * 			"id": 1,
 * 			"name": "peter",
 * 			"code": 990
 *        },
 *        {
 * 			"id": 2,
 * 			"name": "jack",
 * 			"code": 772
 *        }
 * 	]
 * }
 * </p>
 */
@Data
public class InterfaceParamDTO implements Serializable {
    private String interfaceUrl;
    private List<ParamDTO> interfaceDTOS;

}
