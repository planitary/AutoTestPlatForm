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
import com.planitary.atplatform.mapper.ATPlatformTCSExtractParamMapper;
import com.planitary.atplatform.model.dto.AddCaseSetDTO;
import com.planitary.atplatform.model.dto.ExtractParamDTO;
import com.planitary.atplatform.model.dto.QueryCaseSetListDTO;
import com.planitary.atplatform.model.po.ATPlatformCaseSet;
import com.planitary.atplatform.model.po.ATPlatformInterfaceInfo;
import com.planitary.atplatform.model.po.ATPlatformProject;
import com.planitary.atplatform.model.po.ATPlatformTCSExtractParam;
import com.planitary.atplatform.service.caseSet.CaseSetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private ATPlatformTCSExtractParamMapper atPlatformTCSExtractParamMapper;

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
        log.debug("插入集合成功");

        // 解析extractParam并持久化
        ATPlatformTCSExtractParam atPlatformTCSExtractParam = new ATPlatformTCSExtractParam();
        List<ExtractParamDTO> extractParamDTOS = addCaseSetDTO.getExtractParamDTOS();
        for (ExtractParamDTO extractParamDTO : extractParamDTOS){
            String bizId = uniqueStringIdGenerator.idGenerator().substring(3,6) + uniqueStringIdGenerator.idGenerator();
            atPlatformTCSExtractParam.setInterfaceId(extractParamDTO.getInterfaceId());
            atPlatformTCSExtractParam.setBizId(bizId);
            atPlatformTCSExtractParam.setOwner(addCaseSetDTO.getOwner());
            atPlatformTCSExtractParam.setCaseSetId(setId);
            atPlatformTCSExtractParam.setExtractParams(extractParamDTO.getParams().toString());
            int insertExtractParam = atPlatformTCSExtractParamMapper.insert(atPlatformTCSExtractParam);
            if (insertExtractParam <= 0){
                log.error("执行失败:{}", ExceptionEnum.INSERT_FAILED.getErrMessage());
                ATPlatformException.exceptionCast(ExceptionEnum.INSERT_FAILED);
            }
            log.debug("插入参数集合成功");
        }
        return setId;
    }

    @Override
    public String updateCaseSet(AddCaseSetDTO addCaseSetDTO) {
        if (addCaseSetDTO.getSetId() == null){
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }
        // 校验集合是否存在
        String setId = addCaseSetDTO.getSetId();
        LambdaQueryWrapper<ATPlatformCaseSet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ATPlatformCaseSet::getSetId, setId);

        ATPlatformCaseSet caseSet = atPlatformCaseSetMapper.selectOne(lambdaQueryWrapper);
        if (caseSet == null) {
            log.error("集合id错误!");
            ATPlatformException.exceptionCast("集合不存在!", ExceptionEnum.BIZ_ERROR.getErrCode());
        }
        UpdateWrapper<ATPlatformCaseSet> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("set_id", setId);

        UpdateWrapper<ATPlatformTCSExtractParam> extractParamUpdateWrapper = new UpdateWrapper<>();

        if (addCaseSetDTO.getSetName() != null) {
            updateWrapper.set("set_name", addCaseSetDTO.getSetName());
        }
        if (addCaseSetDTO.getInterfaceIds() != null) {
            updateWrapper.set("interface_ids", addCaseSetDTO.getInterfaceIds());
        }
        if (addCaseSetDTO.getSetWeight() != null) {
            updateWrapper.set("set_weight", addCaseSetDTO.getSetWeight());
        }
        if (addCaseSetDTO.getRemark() != null) {
            updateWrapper.set("remark", addCaseSetDTO.getRemark());
        }
        if (addCaseSetDTO.getExtractParamDTOS() != null){
            for (ExtractParamDTO extractParamDTO : addCaseSetDTO.getExtractParamDTOS()) {
                String interfaceId = extractParamDTO.getInterfaceId();
                List<String> params = extractParamDTO.getParams();
                extractParamUpdateWrapper.eq("interfaceId",interfaceId).eq("set_id",setId);
                extractParamUpdateWrapper.set("extract_params",params.toString());
                extractParamUpdateWrapper.set("update_time",LocalDateTime.now());
                atPlatformTCSExtractParamMapper.update(null,extractParamUpdateWrapper);
                int extractParamUpdateCount = atPlatformCaseSetMapper.update(null, updateWrapper);
                if (extractParamUpdateCount <= 0) {
                    ATPlatformException.exceptionCast(ExceptionEnum.UPDATE_FAILED);
                }
                log.info("同步更新提取参数成功");
            }
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

    @Override
    public void parameterExtract(Set<String> parameters) {

    }
}
