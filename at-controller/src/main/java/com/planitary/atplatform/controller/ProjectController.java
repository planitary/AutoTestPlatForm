package com.planitary.atplatform.controller;

import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.customResult.PtResult;
import com.planitary.atplatform.base.handler.PageParams;
import com.planitary.atplatform.base.utils.GeneralIdGenerator;
import com.planitary.atplatform.model.dto.AddProjectDTO;
import com.planitary.atplatform.model.dto.QueryProjectDTO;
import com.planitary.atplatform.model.po.ATTestProject;
import com.planitary.atplatform.service.ProjectInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    @PostMapping("/project/addProject")
    public PtResult<?> addProject(){
        AddProjectDTO addProjectDTO = new AddProjectDTO();
        addProjectDTO.setProjectName("test_baga");
        String projectId = GeneralIdGenerator.generateId();
        addProjectDTO.setProjectId(projectId);
        addProjectDTO.setProjectUrl("/cornerstone");
        projectInfoService.insertProject(addProjectDTO);
        Map<String,String> resMap = new HashMap<>();
        resMap.put("msg","success");
        return PtResult.success(resMap);
    }
}
