package com.planitary.atplatform.service.project;

import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.model.po.PageParams;
import com.planitary.atplatform.model.dto.AddProjectDTO;
import com.planitary.atplatform.model.dto.QueryProjectDTO;
import com.planitary.atplatform.model.po.ATPlatformProject;

import java.util.Map;

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
     * @param queryProjectDTO   查询参数
     * @return      项目查询结果集
     */
    PageResult<ATPlatformProject> queryProjectList(QueryProjectDTO queryProjectDTO);

    PageResult<ATPlatformProject> queryProjectList(PageParams pageParams,QueryProjectDTO queryProjectDTO);
    /**
     * 添加羡慕
     * @param addProjectDTO     insert参数
     * @return      结果集
     */
    Map<String,String> insertProject(AddProjectDTO addProjectDTO);

    /**
     * 更新项目
     * @param projectId             项目id
     * @param atPlatformProject     项目实体类
     * @return
     */
    Map<String,String> updateProject(String projectId,ATPlatformProject atPlatformProject);
}
