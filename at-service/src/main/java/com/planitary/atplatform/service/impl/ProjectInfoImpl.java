package com.planitary.atplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.base.handler.PageParams;
import com.planitary.atplatform.base.utils.GeneralIdGenerator;
import com.planitary.atplatform.mapper.ATPlatformProjectMapper;
import com.planitary.atplatform.model.dto.AddProjectDTO;
import com.planitary.atplatform.model.dto.QueryProjectDTO;
import com.planitary.atplatform.model.po.ATPlatformProject;
import com.planitary.atplatform.service.ProjectInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
