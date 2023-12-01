package com.planitary.atplatform.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：QueryProjectDTO
 * @Date：2023/11/29 10:10 下午
 * @Filename：QueryProjectDTO
 * @description：
 */

/**
 * 项目查询参数
 */
@Data
@ToString
public class QueryProjectDTO {
    private String projectName;
    private String projectUrl;
    private String projectId;
}
