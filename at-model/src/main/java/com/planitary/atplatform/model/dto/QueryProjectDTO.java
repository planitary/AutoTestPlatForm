package com.planitary.atplatform.model.dto;

import com.planitary.atplatform.model.po.PageParams;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：QueryProjectDTO
 * @Date：2023/11/29 10:10 下午
 * @Filename：QueryProjectDTO
 * @description：
 **/

/**
 * 项目查询参数
 * 继承分页属性
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class QueryProjectDTO extends PageParams {
    private String projectName;
    private String projectUrl;
    private String projectId;
}
