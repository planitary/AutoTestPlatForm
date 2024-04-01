package com.planitary.atplatform.controller.project;

import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.customResult.PtResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.model.dto.BaseProjectDTO;
import com.planitary.atplatform.model.po.PageParams;
import com.planitary.atplatform.model.dto.AddProjectDTO;
import com.planitary.atplatform.model.dto.QueryProjectDTO;
import com.planitary.atplatform.model.po.ATPlatformProject;
import com.planitary.atplatform.service.project.ProjectInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping("/project/projectList")
    public PageResult<ATPlatformProject> getProjectList(@RequestBody QueryProjectDTO queryProjectDTO){
        return projectInfoService.queryProjectList(queryProjectDTO);
    }

    @RequestMapping("/mock/projectList")
    public PageResult<ATPlatformProject> getProjectMock(PageParams pageParams){
        pageParams.setPageNo(1L);
        pageParams.setPageSize(10L);
        QueryProjectDTO queryProjectDTO = new QueryProjectDTO();
        queryProjectDTO.setProjectName("test");
        return projectInfoService.queryProjectList(pageParams,queryProjectDTO);
    }

    @PostMapping("/mock/addProject")
    public PtResult<Map<String,String>> addProject(){
        AddProjectDTO addProjectDTO = new AddProjectDTO();
        addProjectDTO.setProjectName("new_test");
        addProjectDTO.setProjectUrl("/HRMS");
        projectInfoService.insertProject(addProjectDTO);
        Map<String,String> resMap = new HashMap<>();
        resMap.put("msg","success");
        return PtResult.success(resMap);
    }

    @PostMapping("/project/addProject")
    public PtResult<?> addProject(@RequestBody AddProjectDTO addProjectDTO){
        Map<String, String> resMap = projectInfoService.insertProject(addProjectDTO);
        return PtResult.success(resMap);
    }

    @PostMapping("/project/updateProject")
    public PtResult<?> updateProject(@RequestBody ATPlatformProject atPlatformProject){
        if (atPlatformProject.getProjectId() == null){
            log.error("项目id为空!");
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }
        Map<String, String> resMap = projectInfoService.updateProject(atPlatformProject.getProjectId(), atPlatformProject);
        return PtResult.success(resMap);
    }

    @PostMapping("/project/getProjectDetail")
    public PtResult<?> getProjectDetail(@RequestBody BaseProjectDTO baseProjectDTO){
        ATPlatformProject project = projectInfoService.getProjectInfo(baseProjectDTO.getProjectId());
        return PtResult.success(project);
    }
}
