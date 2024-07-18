package com.planitary.atplatform.service.caseSet.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayway.jsonpath.JsonPath;
import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.mapper.ATPlatformTCSInterfaceMapper;
import com.planitary.atplatform.model.po.*;
import com.planitary.atplatform.base.utils.UniqueStringIdGenerator;
import com.planitary.atplatform.mapper.ATPlatformCaseSetMapper;
import com.planitary.atplatform.mapper.ATPlatformInterfaceInfoMapper;
import com.planitary.atplatform.mapper.ATPlatformProjectMapper;
import com.planitary.atplatform.model.dto.*;
import com.planitary.atplatform.service.caseSet.CaseSetService;
import com.planitary.atplatform.service.handler.ExecuteHandler;
import com.planitary.atplatform.service.interfaceInfo.InterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    private ATPlatformTCSInterfaceMapper atPlatformTCSInterfaceMapper;

    @Resource
    private InterfaceService interfaceService;

    @Resource
    private ATPlatformInterfaceInfoMapper atPlatformInterfaceInfoMapper;

    @Resource
    private UniqueStringIdGenerator uniqueStringIdGenerator;

    @Resource
    private ExecuteHandler executeHandler;

    @Override
    @Transactional
    public String addCaseSetV1(AddCaseSetDTO addCaseSetDTO) {
        // 校验接口的合法性(数据库in出来的count和列表的size比较）
        // 反序列化interfaceIds
        List<String> interfaceIds = addCaseSetDTO.getInterfaceIds();
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

        if (interfaceIds.isEmpty()) {
            log.error("无接口");
            ATPlatformException.exceptionCast("无接口信息", ExceptionEnum.BIZ_ERROR.getErrCode());
        }

        ATPlatformCaseSet atPlatformCaseSet = new ATPlatformCaseSet();
        String setId = '5' + uniqueStringIdGenerator.idGenerator();

        // 插入set主表
        BeanUtils.copyProperties(addCaseSetDTO, atPlatformCaseSet);
        List<ExtractParamDTO> extractParamDTOS = addCaseSetDTO.getExtractParamDTOS();
        String extractParamDTOJson = JSON.toJSONString(extractParamDTOS);
        atPlatformCaseSet.setParameterList(extractParamDTOJson);
        atPlatformCaseSet.setSetId(setId);
        atPlatformCaseSet.setCreateUser(addCaseSetDTO.getOwner());
        atPlatformCaseSet.setUpdateUser(addCaseSetDTO.getOwner());
        atPlatformCaseSet.setInterfaceIds(addCaseSetDTO.getInterfaceIds().toString());
        // 通过接口id来计算steps数
        atPlatformCaseSet.setSteps(interfaceIds.size());

        int insertCount = atPlatformCaseSetMapper.insert(atPlatformCaseSet);
        if (insertCount <= 0) {
            log.error("执行失败:{}", ExceptionEnum.INSERT_FAILED.getErrMessage());
            ATPlatformException.exceptionCast(ExceptionEnum.INSERT_FAILED);
        }
        log.debug("插入集合成功");
        return setId;
    }

    @Override
    @Transactional
    public String addCaseSet(AddCaseSetDTO addCaseSetDTO) {
        // 校验接口的合法性(数据库in出来的count和列表的size比较）
        // 反序列化interfaceIds
        List<String> interfaceIds = addCaseSetDTO.getInterfaceIds();
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
        String setId = '5' + uniqueStringIdGenerator.idGenerator();

        // 插入set主表
        BeanUtils.copyProperties(addCaseSetDTO, atPlatformCaseSet);
        List<ExtractParamDTO> extractParamDTOS = addCaseSetDTO.getExtractParamDTOS();
        String extractParamDTOJson = JSON.toJSONString(extractParamDTOS);
        atPlatformCaseSet.setParameterList(extractParamDTOJson);
        atPlatformCaseSet.setSetId(setId);
        atPlatformCaseSet.setCreateUser(addCaseSetDTO.getOwner());
        atPlatformCaseSet.setUpdateUser(addCaseSetDTO.getOwner());
        // 插入set-interface关联表
        // 创建关联关系记录
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<ATPlatformTCSInterface> tcsInterfaces = interfaceIds.stream().map(interfaceId -> {
                    ATPlatformTCSInterface atPlatformTCSInterface = new ATPlatformTCSInterface();
                    atPlatformTCSInterface.setSetId(setId);
                    atPlatformTCSInterface.setSetName(addCaseSetDTO.getSetName());
                    atPlatformTCSInterface.setInterfaceId(interfaceId);
                    atPlatformTCSInterface.setCreateTime(LocalDateTime.now().format(formatter));
                    return atPlatformTCSInterface;
                })
                .collect(Collectors.toList());
        Integer batchInsertCount = atPlatformTCSInterfaceMapper.insertBatchTCSInterface(tcsInterfaces);
        log.info("批量插入tcs-interface表,条数:{}", batchInsertCount);

        int insertCount = atPlatformCaseSetMapper.insert(atPlatformCaseSet);
        if (insertCount <= 0) {
            log.error("执行失败:{}", ExceptionEnum.INSERT_FAILED.getErrMessage());
            ATPlatformException.exceptionCast(ExceptionEnum.INSERT_FAILED);
        }
        log.debug("插入集合成功");
        return setId;
    }

    @Override
    @Transactional
    public String updateCaseSetV1(AddCaseSetDTO addCaseSetDTO) {
        if (addCaseSetDTO.getSetId() == null) {
            log.error("集合id为空!");
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
        UpdateWrapper<ATPlatformTCSInterface> tcsDetailDTOUpdateWrapper = new UpdateWrapper<>();

        updateWrapper.eq("set_id", setId);
        tcsDetailDTOUpdateWrapper.eq("set_id", setId);

        // 先更新主表
        if (addCaseSetDTO.getSetName() != null) {
            updateWrapper.set("set_name", addCaseSetDTO.getSetName());
        }
        if (addCaseSetDTO.getInterfaceIds() != null) {
            updateWrapper.set("interface_ids", addCaseSetDTO.getInterfaceIds().toString());
            Integer originalSteps = caseSet.getSteps();
            // steps不一致时，更新
            if (addCaseSetDTO.getInterfaceIds().size() != originalSteps){
                updateWrapper.set("steps",addCaseSetDTO.getInterfaceIds().size());
            }
        }
        if (addCaseSetDTO.getSetWeight() != null) {
            updateWrapper.set("set_weight", addCaseSetDTO.getSetWeight());
        }
        if (addCaseSetDTO.getExtractParamDTOS() != null) {
            String extractParamDTOJson = JSON.toJSONString(addCaseSetDTO.getExtractParamDTOS());
            updateWrapper.set("parameter_list", extractParamDTOJson);
        }
        if (addCaseSetDTO.getRemark() != null) {
            updateWrapper.set("remark", addCaseSetDTO.getRemark());
        }
        if (addCaseSetDTO.getOwner() != null) {
            updateWrapper.set("owner", addCaseSetDTO.getOwner());
        }
        updateWrapper.set("update_time", LocalDateTime.now());
        // 再更新接口表，首先拿到接口对象
        List<InterfaceInfoSIPDTO> interfaceInfoSIPDTOS = addCaseSetDTO.getInterfaceInfoSIPDTOS();
        // 接口更新标记
        boolean interfaceBatchUpdateFlag = true;
        if (!interfaceInfoSIPDTOS.isEmpty()) {
            for (InterfaceInfoSIPDTO interfaceInfoSIPDTO : interfaceInfoSIPDTOS) {
                ATPlatformInterfaceInfo atPlatformInterfaceInfo = new ATPlatformInterfaceInfo();
                BeanUtils.copyProperties(interfaceInfoSIPDTO, atPlatformInterfaceInfo);
                atPlatformInterfaceInfo.setProjectId(addCaseSetDTO.getProjectId());
                Map<String, String> resMap = interfaceService.updateInterfaceV2(atPlatformInterfaceInfo);
                if (!Objects.equals(resMap.get("status"), "success")) {
                    interfaceBatchUpdateFlag = false;
                }
            }
        }
        int updateCount = atPlatformCaseSetMapper.update(null, updateWrapper);
        if (updateCount <= 0 && !interfaceBatchUpdateFlag) {
            ATPlatformException.exceptionCast(ExceptionEnum.UPDATE_FAILED);
        }
        log.info("更新用例集合成功");
        return setId;
    }

    @Override
    @Transactional
    public String updateCaseSet(AddCaseSetDTO addCaseSetDTO) {
        if (addCaseSetDTO.getSetId() == null) {
            log.error("集合id为空!");
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
        UpdateWrapper<ATPlatformTCSInterface> tcsDetailDTOUpdateWrapper = new UpdateWrapper<>();

        updateWrapper.eq("set_id", setId);
        tcsDetailDTOUpdateWrapper.eq("set_id", setId);

        // 先更新主表
        if (addCaseSetDTO.getSetName() != null) {
            updateWrapper.set("set_name", addCaseSetDTO.getSetName());
        }
        if (addCaseSetDTO.getInterfaceIds() != null) {
            updateWrapper.set("interface_ids", addCaseSetDTO.getInterfaceIds());
        }
        if (addCaseSetDTO.getSetWeight() != null) {
            updateWrapper.set("set_weight", addCaseSetDTO.getSetWeight());
        }
        if (addCaseSetDTO.getExtractParamDTOS() != null) {
            String extractParamDTOJson = JSON.toJSONString(addCaseSetDTO.getExtractParamDTOS());
            updateWrapper.set("parameter_list", extractParamDTOJson);
        }
        if (addCaseSetDTO.getRemark() != null) {
            updateWrapper.set("remark", addCaseSetDTO.getRemark());
        }
        if (addCaseSetDTO.getOwner() != null) {
            updateWrapper.set("owner", addCaseSetDTO.getOwner());
        }
        updateWrapper.set("update_time", LocalDateTime.now());

        /* 先计算前端传递的interfaceIds和db的结果，根据结果的不同处理不同：
        1、存在差集，且前端传递的列表 > db的列表，说明页面有新增接口，此时先插入，再更新剩余的
        2、存在差集，且前端传递的列表 < db的列表，说明页面有删减接口，此时先删除，再更新剩余的
        3、存在差集，且
         */
        // 再更新关联表（注意这里需要更新接口）
        //todo 注意这里更新的表是atplatform_tcs_interface,但是实际的update操作需要再mapper中自己实现
//        List<InterfaceInfoSIPDTO> interfaceInfoSIPDTOS = addCaseSetDTO.getInterfaceInfoSIPDTOS().stream().
//                map(interfaceSIPDTO -> {
//            InterfaceInfoSIPDTO interfaceInfoSIPDTO = new InterfaceInfoSIPDTO();
//            interfaceInfoSIPDTO.setInterfaceStatus(interfaceSIPDTO.getInterfaceStatus());
//            interfaceInfoSIPDTO.setInterfaceId(interfaceSIPDTO.getInterfaceId());
//            interfaceInfoSIPDTO.setInterfaceUrl(interfaceSIPDTO.getInterfaceUrl());
//            interfaceInfoSIPDTO.setInterfaceName(interfaceSIPDTO.getInterfaceName());
//            interfaceInfoSIPDTO.setRemark(interfaceSIPDTO.getRemark());
//            interfaceInfoSIPDTO.setRequestBody(interfaceSIPDTO.getRequestBody());
//            return interfaceInfoSIPDTO;
//        }).collect(Collectors.toList());
//        tcsDetailDTOUpdateWrapper.set("")
        tcsDetailDTOUpdateWrapper.set("set_name", addCaseSetDTO.getSetName());

        int updateCount = atPlatformCaseSetMapper.update(null, updateWrapper);
        int tcsUpdateCount = atPlatformTCSInterfaceMapper.update(null, tcsDetailDTOUpdateWrapper);
        if (updateCount <= 0) {
            ATPlatformException.exceptionCast(ExceptionEnum.UPDATE_FAILED);
        }
        log.info("更新用例集合成功");
        return setId;
    }

    @Override
    public PageResult<CaseSetWithProjectDTO> queryCaseSetList(QueryCaseSetListDTO queryCaseSetListDTO) {
        String projectId = queryCaseSetListDTO.getProjectId();
        final String SUCCESS_CODE = "200";

        if (!Objects.equals(queryCaseSetListDTO.getProjectId(), "") && queryCaseSetListDTO.getProjectId() != null) {
            LambdaQueryWrapper<ATPlatformProject> atPlatformProjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            atPlatformProjectLambdaQueryWrapper.eq(ATPlatformProject::getProjectId, projectId);
            ATPlatformProject atPlatformProject = atPlatformProjectMapper.selectOne(atPlatformProjectLambdaQueryWrapper);
            if (atPlatformProject == null) {
                log.error("project不存在");
                ATPlatformException.exceptionCast("接口所属项目不存在");
            }
        }

        long pageNo = queryCaseSetListDTO.getPageNo();
        long pageSize = queryCaseSetListDTO.getPageSize();
        if (pageNo <= 0 || pageSize <= 0) {
            ATPlatformException.exceptionCast(ExceptionEnum.PAGINATION_PARAM_ERROR);
        }
        Page<CaseSetWithProjectDTO> page = new Page<>(pageNo, pageSize);
        Page<CaseSetWithProjectDTO> caseSetPage = atPlatformCaseSetMapper.getCaseSetList(page, queryCaseSetListDTO);
        List<CaseSetWithProjectDTO> records = caseSetPage.getRecords();
        long total = caseSetPage.getTotal();
        log.info("查询到的casesetList总数:{}", total);
        return new PageResult<>(records, total, pageNo, pageSize, SUCCESS_CODE);

    }

    @Override
    public void doCaseSetCore(String caseSetId) {
        LambdaQueryWrapper<ATPlatformCaseSet> caseSetLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<ATPlatformInterfaceInfo> interfaceInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        caseSetLambdaQueryWrapper.eq(ATPlatformCaseSet::getSetId, caseSetId);
        ATPlatformCaseSet atPlatformCaseSet = atPlatformCaseSetMapper.selectOne(caseSetLambdaQueryWrapper);
        if (atPlatformCaseSet == null) {
            log.error("测试集合不存在!");
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }

        // 公共Map，存放通过提取的参数获得的值
        Map<String, Object> extractValueMap = new HashMap<>();
        // 序列化成json后反序列化为ExtractParamDTO实体类
        String parameterList = atPlatformCaseSet.getParameterList();
        log.info(parameterList);
        List<ExtractParamDTO> extractParamDTOS = JSON.parseArray(parameterList, ExtractParamDTO.class);
        // 接口列表依次发起调用，然后根据提取参数，对该接口的返回值进行jsonPath读取
        String interfaceIdsJson = atPlatformCaseSet.getInterfaceIds();
        log.debug(interfaceIdsJson);
        List<String> interfaceIds = JSON.parseArray(interfaceIdsJson, String.class);
        //  开始执行
        for (String interfaceId : interfaceIds) {
            LambdaQueryWrapper<ATPlatformInterfaceInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ATPlatformInterfaceInfo::getInterfaceId, interfaceId);
            ATPlatformInterfaceInfo atPlatformInterfaceInfo = atPlatformInterfaceInfoMapper.selectOne(lambdaQueryWrapper);
            if (atPlatformInterfaceInfo == null) {
                ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
            }
            // 每次调用前先判断当前接口的参数有没有公共提取参数列表中的值，有的话覆盖
            String requestBodyJSON = atPlatformInterfaceInfo.getRequestBody();
            Map<String, Object> requestBody = JSON.parseObject(requestBodyJSON);
            for (Map.Entry<String, Object> entry : extractValueMap.entrySet()) {
                if (requestBody.containsKey(entry.getKey())) {
                    requestBody.put(entry.getKey(), entry.getValue());
                }
            }
            // 发起调用
            log.debug("当前发起调用的接口:{}-{}", interfaceId, atPlatformInterfaceInfo.getInterfaceUrl());
            try {

                ExecuteDTO executeDTO = new ExecuteDTO();
                executeDTO.setInterfaceId(interfaceId);
                executeDTO.setInterfaceUrl(atPlatformInterfaceInfo.getInterfaceUrl());
                executeDTO.setProjectId(atPlatformInterfaceInfo.getProjectId());

                executeDTO.setRequestBody(requestBody);
                executeDTO.setRequireTime(System.currentTimeMillis());
                ExecuteResponseDTO executeResponseDTO = executeHandler.doInterfaceExecutor(executeDTO);
                // 调用成功后根据提取列表，提取当前接口的调用结果,由于接口提取参数的接口顺序和调用顺序一致
                // 先判断当前有无需要提取的参数,
                for (ExtractParamDTO extractParamDTO : extractParamDTOS) {
                    if (Objects.equals(interfaceId, extractParamDTO.getInterfaceId())) {
                        List<Map<String, Object>> params = extractParamDTO.getParams();
                        String responseBody = executeResponseDTO.getResponseBody();
                        for (Map<String, Object> param : params) {
                            for (Map.Entry<String, Object> entry : param.entrySet()) {
                                log.debug("提取到参数:{}", param);
                                Object read = JsonPath.read(responseBody, (String) entry.getValue());
                                extractValueMap.put(entry.getKey(), read);
                            }
                        }
                    }
                }
            } catch (ATPlatformException e) {
                log.error("接口调用失败");
                e.printStackTrace();
            }
            log.info("当前接口{}执行完毕", interfaceId);
        }
        log.info("=========调用结束=========");
    }

    @Override
    public TCSDetailDTO getCaseSetDetail(String setId) {
        if (Objects.equals(null, setId) || Objects.equals("", setId)) {
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }

        List<TCSDetailDTO> tcsDetails = atPlatformTCSInterfaceMapper.getTCSDetail(setId);
        TCSDetailDTO tcsDetailDTO = new TCSDetailDTO();
        BeanUtils.copyProperties(tcsDetails.get(0), tcsDetailDTO);
//        tcsDetailDTO.setSetId(setId);
//        tcsDetailDTO.setSetName(tcsDetails.get(0).getSetName());

        LambdaQueryWrapper<ATPlatformProject> projectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        projectLambdaQueryWrapper.eq(ATPlatformProject::getProjectId, tcsDetails.get(0).getProjectId());
        ATPlatformProject atPlatformProject = atPlatformProjectMapper.selectOne(projectLambdaQueryWrapper);
        if (atPlatformProject == null) {
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        tcsDetailDTO.setProjectName(atPlatformProject.getProjectName());

        List<InterfaceInfoSIPDTO> interfaceInfoSIPDTOS = tcsDetails.stream().map(tcsDetail -> {
            InterfaceInfoSIPDTO interfaceInfoSIPDTO = new InterfaceInfoSIPDTO();
            interfaceInfoSIPDTO.setInterfaceId(tcsDetail.getInterfaceId());
            interfaceInfoSIPDTO.setInterfaceName(tcsDetail.getInterfaceName());
            interfaceInfoSIPDTO.setInterfaceUrl(tcsDetail.getInterfaceUrl());
            interfaceInfoSIPDTO.setRequestBody(tcsDetail.getRequestBody());
            interfaceInfoSIPDTO.setRemark(tcsDetail.getRemark());
            return interfaceInfoSIPDTO;
        }).collect(Collectors.toList());

        tcsDetailDTO.setInterfaceInfoSIPDTOS(interfaceInfoSIPDTOS);
        return tcsDetailDTO;
    }

    @Override
    public TCSDetailDTO getCaseSetDetailV1(String setId) {
        if (Objects.equals(null, setId) || Objects.equals("", setId)) {
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }
        LambdaQueryWrapper<ATPlatformCaseSet> caseSetLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<ATPlatformInterfaceInfo> interfaceInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<ATPlatformProject> projectLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //先找到集合
        caseSetLambdaQueryWrapper.eq(ATPlatformCaseSet::getSetId, setId);
        ATPlatformCaseSet atPlatformCaseSet = atPlatformCaseSetMapper.selectOne(caseSetLambdaQueryWrapper);
        if (atPlatformCaseSet == null) {
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        // 提取集合的接口id列表
        String interfaceIds = atPlatformCaseSet.getInterfaceIds();
        List<String> iidList = JSON.parseObject(interfaceIds, new TypeReference<>() {
        });
        TCSDetailDTO tcsDetailDTO = new TCSDetailDTO();
        // 查找项目数据
        projectLambdaQueryWrapper.eq(ATPlatformProject::getProjectId, atPlatformCaseSet.getProjectId());
        ATPlatformProject atPlatformProject = atPlatformProjectMapper.selectOne(projectLambdaQueryWrapper);
        if (atPlatformProject == null) {
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        tcsDetailDTO.setProjectName(atPlatformProject.getProjectName());
        // 拷贝tcs的值
        BeanUtils.copyProperties(atPlatformCaseSet, tcsDetailDTO);

        if (!Objects.equals(null,iidList) && iidList.size() > 0) {
            // 根据接口id，批量查询接口数据
            interfaceInfoLambdaQueryWrapper.in(ATPlatformInterfaceInfo::getInterfaceId, iidList);
            List<ATPlatformInterfaceInfo> atPlatformInterfaceInfos = atPlatformInterfaceInfoMapper.selectList(interfaceInfoLambdaQueryWrapper);
            log.info("找到了接口{}条", atPlatformInterfaceInfos.size());

            // 单独处理接口数据
            List<InterfaceInfoSIPDTO> iiSIPs = atPlatformInterfaceInfos.stream().map(atPlatformInterfaceInfo -> {
                InterfaceInfoSIPDTO interfaceInfoSIPDTO = new InterfaceInfoSIPDTO();
                interfaceInfoSIPDTO.setInterfaceStatus("0");
                interfaceInfoSIPDTO.setInterfaceId(atPlatformInterfaceInfo.getInterfaceId());
                interfaceInfoSIPDTO.setInterfaceUrl(atPlatformInterfaceInfo.getInterfaceUrl());
                interfaceInfoSIPDTO.setInterfaceName(atPlatformInterfaceInfo.getInterfaceName());
                interfaceInfoSIPDTO.setRemark(atPlatformInterfaceInfo.getRemark());
                interfaceInfoSIPDTO.setRequestBody(atPlatformInterfaceInfo.getRequestBody());
                return interfaceInfoSIPDTO;
            }).collect(Collectors.toList());
            tcsDetailDTO.setInterfaceInfoSIPDTOS(iiSIPs);
            log.info("tcs详情:{}", tcsDetailDTO);
        }
        else {
            tcsDetailDTO.setInterfaceInfoSIPDTOS(null);
        }
        return tcsDetailDTO;
    }


}
