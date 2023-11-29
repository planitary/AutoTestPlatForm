package com.planitary.atplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.base.handle.PageParams;
import com.planitary.atplatform.mapper.ATTestProjectMapper;
import com.planitary.atplatform.model.dto.QueryProjectDTO;
import com.planitary.atplatform.model.po.ATTestProject;
import com.planitary.atplatform.service.ProjectInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Resource
    ATTestProjectMapper atTestProjectMapper;

    @Override
    public PageResult<ATTestProject> queryProjectList(PageParams pageParams, QueryProjectDTO queryProjectDTO) {
        LambdaQueryWrapper<ATTestProject> atTestProjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 拼接查询条件
        atTestProjectLambdaQueryWrapper.eq(ATTestProject::getProjectName,queryProjectDTO.getProjectName())
                .like(StringUtils.isNotEmpty(queryProjectDTO.getInterfaceName()),ATTestProject::getInterfaceName,queryProjectDTO.getInterfaceName())
                .eq(ATTestProject::getInterfaceUrl,queryProjectDTO.getInterfaceUrl());

        // 分页参数
        long pageNo = pageParams.getPageNo();
        long pageSize = pageParams.getPageSize();
        if (pageNo <= 0 || pageSize <= 0){
            ATPlatformException.exceptionCast(ExceptionEnum.PAGINATION_PARAM_ERROR);
        }

        Page<ATTestProject> page = new Page<>(pageNo,pageSize);
        // 分页查询
        Page<ATTestProject> projectPage = atTestProjectMapper.selectPage(page, atTestProjectLambdaQueryWrapper);
        // 数据列表
        List<ATTestProject> records = projectPage.getRecords();
        long total = projectPage.getTotal();
        return new PageResult<>(records,total,pageNo,pageNo,SUCCESS_CODE);
    }
}
