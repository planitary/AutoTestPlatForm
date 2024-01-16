package com.planitary.atplatform.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：ChosenParamDTO
 * @Date：2023/12/28 10:41 下午
 * @Filename：ChosenParamDTO
 * @description：        封装了用户选中的接口，带接口url的json，典型的DTO如下
 * {
 * 	"chosenParamDTOs": [
 *                {
 * 			"interfaceUrl": "/hera/core/list",
 * 			"interfaceParamDTOs": [    * 	{
 * 						"page": 7,
 * 						"size": 25,
 * 						"res": [10,20,30]
 *
 *                },
 *                 {
 * 						"page": 6,
 * 						"size": 70,
 * 						"res": [4,6,9]
 *
 *                }
 * 			]
 *        },
 *        {
 * 			"interfaceUrl": "/hermrs/core/log",
 * 			"interfaceParamDTOs": [
 *         {
 * 						"ocCode": "S12332",
 * 						"ocName": "testStation"
 *
 *                }
 * 			]
 *        }
 * 	]
 * }
 */
@Data
public class ChosenParamDTO implements Serializable {
    private List<InterfaceParamDTO> chosenParamDTOs;
    private String callMethod;
}
