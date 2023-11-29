package com.planitary.atplatform.service;

import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.handle.PageParams;
import com.planitary.atplatform.model.dto.QueryProjectDTO;
import com.planitary.atplatform.model.po.ATTestProject;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.service
 * @name：ProjectService
 * @Date：2023/11/29 10:07 下午
 * @Filename：ProjectService
 * @description：
 */
public interface ProjectInfoService {
    /**
     *  项目查询
     * @param pageParams        分页参数
     * @param queryProjectDTO   查询参数
     * @return      项目查询结果集
     */
    PageResult<ATTestProject> queryProjectList(PageParams pageParams, QueryProjectDTO queryProjectDTO);
}
