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
 * 	"paramDTOS": [
 *                {
 * 			"interfaceUrl": "cornerstone/test",
 * 			"interfaceDTOS": [
 *                {
 * 					"id": 1,
 * 					"name": "peter",
 * 					"code": 990
 *                },
 *                {
 * 					"id": 2,
 * 					"name": "jack",
 * 					"code": 772
 *                }
 * 			]
 *        },
 *        {
 * 			"interfaceUrl": "prism/get",
 * 			"interfaceDTOS": [
 *                {
 * 					"ocCode": "S12377",
 * 					"ocName": "TEST-s"
 *                },
 *                {
 * 					"ocCode": "S12375",
 * 					"ocName": "TEST-x"
 *                }
 * 			]
 *        }
 * 	]
 * }
 */
@Data
public class ChosenParamDTO implements Serializable {
    private List<InterfaceParamDTO> chosenParamDTOs;
}
