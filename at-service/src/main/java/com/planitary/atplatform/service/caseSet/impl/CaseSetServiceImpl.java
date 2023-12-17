package com.planitary.atplatform.service.caseSet.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.base.handler.PageParams;
import com.planitary.atplatform.base.utils.GeneralIdGenerator;
import com.planitary.atplatform.base.utils.UniqueStringIdGenerator;
import com.planitary.atplatform.mapper.ATPlatformCaseSetMapper;
import com.planitary.atplatform.mapper.ATPlatformInterfaceInfoMapper;
import com.planitary.atplatform.mapper.ATPlatformProjectMapper;
import com.planitary.atplatform.model.dto.AddCaseSetDTO;
import com.planitary.atplatform.model.dto.QueryCaseSetListDTO;
import com.planitary.atplatform.model.po.ATPlatformCaseSet;
import com.planitary.atplatform.model.po.ATPlatformInterfaceInfo;
import com.planitary.atplatform.model.po.ATPlatformProject;
import com.planitary.atplatform.service.caseSet.CaseSetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.service.caseSet.impl
 * @name：CaseSetServiceImpl
 * @Date：2023/12/14 10:15 下午
 * @Filename：CaseSetServiceImpl
 * @description：
 */
@Service
@Slf4j
public class CaseSetServiceImpl implements CaseSetService {
    @Resource
    private ATPlatformCaseSetMapper atPlatformCaseSetMapper;
    @Resource
    private ATPlatformProjectMapper atPlatformProjectMapper;

    @Resource
    private ATPlatformInterfaceInfoMapper atPlatformInterfaceInfoMapper;

    @Resource
    private UniqueStringIdGenerator uniqueStringIdGenerator;

    @Override
    public String addCaseSet(AddCaseSetDTO addCaseSetDTO) {
        // 校验接口的合法性(数据库in出来的count和列表的size比较）
        // 反序列化interfaceIds
        List<String> interfaceIds = JSON.parseArray(addCaseSetDTO.getInterfaceIds(), String.class);
        QueryWrapper<ATPlatformInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("interface_id", interfaceIds);

        // 校验项目的合法性
        String projectId = addCaseSetDTO.getProjectId();
        LambdaQueryWrapper<ATPlatformProject> projectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        projectLambdaQueryWrapper.eq(ATPlatformProject::getProjectId, projectId);
        ATPlatformProject project = atPlatformProjectMapper.selectOne(projectLambdaQueryWrapper);
        if (project == null) {
            log.error("项目:{}不存在", projectId);
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }

        List<ATPlatformInterfaceInfo> atPlatformInterfaceInfos = atPlatformInterfaceInfoMapper.selectList(queryWrapper);
        if (atPlatformInterfaceInfos.size() != interfaceIds.size()) {
            log.error("接口数量不一致!");
            ATPlatformException.exceptionCast("接口有缺失,请核对!");
        }
        if (addCaseSetDTO.getSetWeight() < 0) {
            log.error("权重设置异常");
            ATPlatformException.exceptionCast("权重不能为负!", ExceptionEnum.BIZ_ERROR.getErrCode());
        }

        if (interfaceIds.size() == 0) {
            log.error("无接口");
            ATPlatformException.exceptionCast("无接口信息", ExceptionEnum.BIZ_ERROR.getErrCode());
        }

        ATPlatformCaseSet atPlatformCaseSet = new ATPlatformCaseSet();
        String setId = uniqueStringIdGenerator.idGenerator().substring(2, 4) + uniqueStringIdGenerator.idGenerator();

        // 封装interfaceIds和parameterList为json
//        String interfaceIdsJSON = JSON.toJSONString(interfaceIds);
//        String parameterListJSON = JSON.toJSONString(addCaseSetDTO.getParameterList());
        BeanUtils.copyProperties(addCaseSetDTO, atPlatformCaseSet);
//        atPlatformCaseSet.setSetName(addCaseSetDTO.getSetName());
//        atPlatformCaseSet.setSetWeight(addCaseSetDTO.getWeight());
//        atPlatformCaseSet.setInterfaceIds(interfaceIdsJSON);
//        atPlatformCaseSet.setParameterList(parameterListJSON);
        atPlatformCaseSet.setSetId(setId);
        atPlatformCaseSet.setCreateUser("planitary");
        atPlatformCaseSet.setUpdateUser("planitary");

        int insertCount = atPlatformCaseSetMapper.insert(atPlatformCaseSet);
        if (insertCount <= 0) {
            log.error("执行失败:{}", ExceptionEnum.INSERT_FAILED.getErrMessage());
            ATPlatformException.exceptionCast(ExceptionEnum.INSERT_FAILED);
        }
        log.debug("插入成功");
        return setId;
    }

    @Override
    public String updateCaseSet(ATPlatformCaseSet atPlatformCaseSet) {
        // 校验集合是否存在
        String setId = atPlatformCaseSet.getSetId();
        LambdaQueryWrapper<ATPlatformCaseSet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ATPlatformCaseSet::getSetId, setId);
        ATPlatformCaseSet caseSet = atPlatformCaseSetMapper.selectOne(lambdaQueryWrapper);
        if (caseSet == null) {
            log.error("集合id错误!");
            ATPlatformException.exceptionCast("集合不存在!", ExceptionEnum.BIZ_ERROR.getErrCode());
        }
        UpdateWrapper<ATPlatformCaseSet> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("set_id", setId);

        if (atPlatformCaseSet.getSetName() != null) {
            updateWrapper.set("set_name", atPlatformCaseSet.getSetName());
        }
        if (atPlatformCaseSet.getInterfaceIds() != null) {
            updateWrapper.set("interface_ids", atPlatformCaseSet.getInterfaceIds());
        }
        if (atPlatformCaseSet.getSetWeight() != null) {
            updateWrapper.set("set_weight", atPlatformCaseSet.getSetWeight());
        }
        if (atPlatformCaseSet.getParameterList() != null) {
            updateWrapper.set("patameter_list", atPlatformCaseSet.getParameterList());
        }
        if (atPlatformCaseSet.getRemark() != null) {
            updateWrapper.set("remark", atPlatformCaseSet.getRemark());
        }
        updateWrapper.set("update_time", LocalDateTime.now());

        int updateCount = atPlatformCaseSetMapper.update(null, updateWrapper);
        if (updateCount <= 0) {
            ATPlatformException.exceptionCast(ExceptionEnum.UPDATE_FAILED);
        }
        log.info("更新接口成功");
        return setId;
    }

    @Override
    public PageResult<ATPlatformCaseSet> queryCaseSetList(PageParams pageParams, QueryCaseSetListDTO queryCaseSetListDTO) {
        String projectId = queryCaseSetListDTO.getProjectId();
        if (projectId == null) {
            log.error("projectId不能为空");
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }
        LambdaQueryWrapper<ATPlatformProject> atPlatformProjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        atPlatformProjectLambdaQueryWrapper.eq(ATPlatformProject::getProjectId, projectId);
        ATPlatformProject atPlatformProject = atPlatformProjectMapper.selectOne(atPlatformProjectLambdaQueryWrapper);
        if (atPlatformProject == null) {
            log.error("project不存在");
            ATPlatformException.exceptionCast("项目不存在");
        }
        LambdaQueryWrapper<ATPlatformCaseSet> atPlatformCaseSetLambdaQueryWrapper = new LambdaQueryWrapper<>();
        atPlatformCaseSetLambdaQueryWrapper
                .like(StringUtils.isNotEmpty(queryCaseSetListDTO.getSetName()),
                        ATPlatformCaseSet::getSetName, queryCaseSetListDTO.getSetName())
                .like(StringUtils.isNotEmpty(queryCaseSetListDTO.getInterfaceIds()),
                        ATPlatformCaseSet::getInterfaceIds, queryCaseSetListDTO.getInterfaceIds());
        long pageNo = pageParams.getPageNo();
        long pageSize = pageParams.getPageSize();
        if (pageNo <= 0 || pageSize <= 0){
            ATPlatformException.exceptionCast(ExceptionEnum.PAGINATION_PARAM_ERROR);
        }
        Page<ATPlatformCaseSet> page = new Page<>(pageNo,pageSize);
        Page<ATPlatformCaseSet> caseSetPage = atPlatformCaseSetMapper.selectPage(page,atPlatformCaseSetLambdaQueryWrapper);
        List<ATPlatformCaseSet> records = caseSetPage.getRecords();
        long total = caseSetPage.getTotal();
        log.info("查询到的记录总数:{}",total);
        return new PageResult<>(records,total,pageNo,pageSize,"200");
    }
}
