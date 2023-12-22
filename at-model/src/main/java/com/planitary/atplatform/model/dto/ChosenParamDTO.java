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
 * 封装选择的接口参数列表（带接口url）
 * 一个典型的DTO格式为：
 * <p>
 * {
 * 	"cornerstone/test": [
 *                {
 * 			"id": 1,
 * 			"name": "jack",
 * 			"age": 20
 *        },
 *        {
 * 			"id": 2,
 * 			"name": "peter",
 * 			"age": 33
 *        },
 *         {
 * 			"id": 3,
 * 			"name": "jack",
 * 			"age": 21
 *        }
 * 	],
 * 	"conerstone/login": [
 *         {
 * 			"ids": [
 * 				"2342354235",
 * 				"324823423",
 * 				"214301924312"
 * 			],
 * 			"token": "2288e"
 *        },
 *        {
 * 			"ids": [
 * 				"3247328423",
 * 				"21481231"
 * 			],
 * 			"token": "77u8"
 *        }
 * 	]
 * }
 * </p>
 */
@Data
public class ChosenParamDTO {
    private Map<String,List<ParamDTO>> chosenParamDTO;

}
