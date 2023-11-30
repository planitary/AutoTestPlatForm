package com.planitary.atplatform.controller;

import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.handler.PageParams;
import com.planitary.atplatform.model.dto.QueryProjectDTO;
import com.planitary.atplatform.model.po.ATTestProject;
import com.planitary.atplatform.service.ProjectInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.controller
 * @name：ProjectController
 * @Date：2023/11/29 10:57 下午
 * @Filename：ProjectController
 * @description：
 */
@RestController
@Slf4j
public class ProjectController {

    @Resource
    ProjectInfoService projectInfoService;

    @GetMapping("/project/projectList")
    public PageResult<ATTestProject> getProjectList(PageParams pageParams, @RequestBody QueryProjectDTO queryProjectDTO){
        return projectInfoService.queryProjectList(pageParams, queryProjectDTO);
    }

    @RequestMapping("/mock/projectList")
    public PageResult<ATTestProject> getProjectMock(PageParams pageParams){
        pageParams.setPageNo(1L);
        pageParams.setPageSize(10L);
        QueryProjectDTO queryProjectDTO = new QueryProjectDTO();
        queryProjectDTO.setProjectName("test");
        return projectInfoService.queryProjectList(pageParams,queryProjectDTO);
    }
}
