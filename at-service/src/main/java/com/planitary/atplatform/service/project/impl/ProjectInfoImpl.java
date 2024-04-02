package com.planitary.atplatform.service.project.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.model.po.PageParams;
import com.planitary.atplatform.base.utils.GeneralIdGenerator;
import com.planitary.atplatform.mapper.ATPlatformProjectMapper;
import com.planitary.atplatform.model.dto.AddProjectDTO;
import com.planitary.atplatform.model.dto.QueryProjectDTO;
import com.planitary.atplatform.model.po.ATPlatformProject;
import com.planitary.atplatform.service.project.ProjectInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.service.impl
 * @name：ProjectImpl
 * @Date：2023/11/29 10:12 下午
 * @Filename：ProjectImpl
 * @description：
 */
@Service
@Slf4j
public class ProjectInfoImpl implements ProjectInfoService {
    private final String SUCCESS_CODE = "200";

    private final Integer INIT_VERSION = 1;

    @Resource
    ATPlatformProjectMapper atPlatformProjectMapper;

    @Override
    public PageResult<ATPlatformProject> queryProjectList(QueryProjectDTO queryProjectDTO) {
        LambdaQueryWrapper<ATPlatformProject> atTestProjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 拼接查询条件
        atTestProjectLambdaQueryWrapper.like(StringUtils.isNotEmpty(queryProjectDTO.getProjectName()),
                ATPlatformProject::getProjectName,queryProjectDTO.getProjectName());
        atTestProjectLambdaQueryWrapper.like(StringUtils.isNotEmpty(queryProjectDTO.getProjectUrl()),
                ATPlatformProject::getProjectUrl,queryProjectDTO.getProjectUrl());

        // 按照更新时间倒序排序
        atTestProjectLambdaQueryWrapper.eq(StringUtils.isNotEmpty(queryProjectDTO.getProjectId()),
                ATPlatformProject::getProjectId,queryProjectDTO.getProjectId())
                .orderByDesc(ATPlatformProject::getUpdateTime);

        // 分页参数
        long pageNo = queryProjectDTO.getPageNo();
        long pageSize = queryProjectDTO.getPageSize();
        if (pageNo <= 0 || pageSize <= 0){
            ATPlatformException.exceptionCast(ExceptionEnum.PAGINATION_PARAM_ERROR);
        }

        Page<ATPlatformProject> page = new Page<>(pageNo,pageSize);
        // 分页查询
        Page<ATPlatformProject> projectPage = atPlatformProjectMapper.selectPage(page,atTestProjectLambdaQueryWrapper);
        // 数据列表
        List<ATPlatformProject> records = projectPage.getRecords();

        long total = projectPage.getTotal();
        log.info("查询到的记录总数:{}",total);
        return new PageResult<>(records,total,pageNo,pageSize,SUCCESS_CODE);
    }

    @Override
    public PageResult<ATPlatformProject> queryProjectList(PageParams pageParams, QueryProjectDTO queryProjectDTO) {
        LambdaQueryWrapper<ATPlatformProject> atTestProjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 拼接查询条件
        atTestProjectLambdaQueryWrapper.like(StringUtils.isNotEmpty(queryProjectDTO.getProjectName()),
                ATPlatformProject::getProjectName,queryProjectDTO.getProjectName());
        atTestProjectLambdaQueryWrapper.like(StringUtils.isNotEmpty(queryProjectDTO.getProjectUrl()),
                ATPlatformProject::getProjectUrl,queryProjectDTO.getProjectUrl());

        atTestProjectLambdaQueryWrapper.eq(StringUtils.isNotEmpty(queryProjectDTO.getProjectId()),
                ATPlatformProject::getProjectId,queryProjectDTO.getProjectId());

        // 分页参数
        long pageNo = pageParams.getPageNo();
        long pageSize = pageParams.getPageSize();
        if (pageNo <= 0 || pageSize <= 0){
            ATPlatformException.exceptionCast(ExceptionEnum.PAGINATION_PARAM_ERROR);
        }

        Page<ATPlatformProject> page = new Page<>(pageNo,pageSize);
        // 分页查询
        Page<ATPlatformProject> projectPage = atPlatformProjectMapper.selectPage(page,atTestProjectLambdaQueryWrapper);
        // 数据列表
        List<ATPlatformProject> records = projectPage.getRecords();
        long total = projectPage.getTotal();
        log.info("查询到的记录总数:{}",total);
        return new PageResult<>(records,total,pageNo,pageSize,SUCCESS_CODE);
    }

    @Override
    public Map<String,String> insertProject(AddProjectDTO addProjectDTO) {
        int currentVersion = INIT_VERSION;
        // 拷贝相同值
        ATPlatformProject atPlatformProject = new ATPlatformProject();
        BeanUtils.copyProperties(addProjectDTO, atPlatformProject);
        atPlatformProject.setVersion(currentVersion);
        String projectId = GeneralIdGenerator.generateId() + GeneralIdGenerator.generateId().substring(1, 7);
        atPlatformProject.setCreateUser("zane");
        atPlatformProject.setVersion(1);
        atPlatformProject.setUpdateUser("zane");
        atPlatformProject.setProjectId(projectId);
        int insertCount = atPlatformProjectMapper.insert(atPlatformProject);
        if (insertCount <= 0){
            log.error("执行失败:{}",ExceptionEnum.INSERT_FAILED.getErrMessage());
            ATPlatformException.exceptionCast(ExceptionEnum.INSERT_FAILED);
        }
        log.info("插入成功");
        Map<String,String> resMap = new HashMap<>();
        resMap.put("projectId",projectId);
//        //  插入后更新版本号
//        currentVersion ++;
        return resMap;
    }

    @Override
    @Transactional
    public Map<String, String> updateProject(String projectId, ATPlatformProject atPlatformProject) {
        boolean versionFlag = false;
        // 校验id合法性
        LambdaQueryWrapper<ATPlatformProject> atPlatformProjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        atPlatformProjectLambdaQueryWrapper.eq(ATPlatformProject::getProjectId,projectId);
        ATPlatformProject project = atPlatformProjectMapper.selectOne(atPlatformProjectLambdaQueryWrapper);
        if (project == null){
            log.error("项目:{}不存在",projectId);
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        // 更新项目后，version版本更新（version仅在字段更新时更新)
        Integer originVersion = project.getVersion();
        UpdateWrapper<ATPlatformProject> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("project_id",projectId);
        if (!Objects.equals(project.getProjectName(),atPlatformProject.getProjectName())) {
            project.setProjectName(atPlatformProject.getProjectName());
            versionFlag = true;
        }
        if (!Objects.equals(project.getProjectUrl(),atPlatformProject.getProjectUrl())) {
            project.setProjectUrl(atPlatformProject.getProjectUrl());
            versionFlag = true;
        }
        if (!Objects.equals(project.getProjectGroup(),atPlatformProject.getProjectGroup())) {
            project.setProjectGroup(atPlatformProject.getProjectGroup());
            versionFlag = true;
        }
        if (!Objects.equals(project.getProjectOwner(),atPlatformProject.getProjectOwner())) {
            project.setProjectOwner(atPlatformProject.getProjectOwner());
            versionFlag = true;
        }
        if (!Objects.equals(project.getProjectLevel(),atPlatformProject.getProjectLevel())) {
            project.setProjectLevel(atPlatformProject.getProjectLevel());
            versionFlag = true;
        }
        if (!Objects.equals(project.getRemark(),atPlatformProject.getRemark())) {
            project.setRemark(atPlatformProject.getRemark());
            versionFlag = true;
        }
        if (versionFlag) {
            project.setVersion(originVersion + 1);

            int updateCount = atPlatformProjectMapper.update(project, updateWrapper);
            if (updateCount <= 0) {
                ATPlatformException.exceptionCast(ExceptionEnum.UPDATE_FAILED);
            }
            log.info("更新项目成功");
        }
        else {
            log.info("项目无变化，无需更新");
        }
        Map<String,String> resMap = new HashMap<>();
        resMap.put("projectId",projectId);
        return resMap;
    }

    @Override
    public ATPlatformProject getProjectInfo(String projectId) {
        if (projectId == null) {
            log.error("projectId不能为空");
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }
        LambdaQueryWrapper<ATPlatformProject> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ATPlatformProject::getProjectId,projectId);
        ATPlatformProject project = atPlatformProjectMapper.selectOne(lambdaQueryWrapper);
        if (project == null){
            log.error("项目不存在");
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        return project;
    }

    @Override
    @Transactional
    public String deleteProject(String projectId) {

        LambdaQueryWrapper<ATPlatformProject> atPlatformProjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        atPlatformProjectLambdaQueryWrapper.eq(ATPlatformProject::getProjectId,projectId);
        ATPlatformProject project = atPlatformProjectMapper.selectOne(atPlatformProjectLambdaQueryWrapper);
        if (project == null){
            log.error("项目:{}不存在",projectId);
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        UpdateWrapper<ATPlatformProject> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("project_id",projectId).set("is_delete",1);
        int updateCount = atPlatformProjectMapper.update(null, updateWrapper);
        if (updateCount <= 0) {
            ATPlatformException.exceptionCast(ExceptionEnum.UPDATE_FAILED);
        }
        return "删除成功!";
    }
}
