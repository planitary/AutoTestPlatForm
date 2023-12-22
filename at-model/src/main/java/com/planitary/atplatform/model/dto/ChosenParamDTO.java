package com.planitary.atplatform.model.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：ChosenParamDTO
 * @Date：2023/12/22 11:00 下午
 * @Filename：ChosenParamDTO
 * @description：
 * 封装选择的接口参数列表
 * 一个典型的DTO格式为：
 * <p>
 * {
 * "cornerstone/loading":
 * {
 * 			"id": [
 * 				1,
 * 				2,
 * 				3,
 * 				4
 * 			],
 * 			"age": [
 * 				34,
 * 				33,
 * 				22,
 * 				12
 * 			],
 * 			"name": [
 * 				"jack",
 * 				"peter",
 * 				"zane"
 * 			]
 *                }
 * 	,
 * 	"cornerstone/logout": [
 *        {
 * 			"id": [
 * 				"24794124",
 * 				"329840214",
 * 				"218740",
 * 				"8213u0"
 * 			],
 * 			"token": [
 * 				"7J",
 * 				"87b"
 * 			]
 *        }
 * 	]
 * }
 * </p>
 */
@Data
public class ChosenParamDTO {
    private Map<String,Map<String,List<Object>>> chosenParamMap;

}
