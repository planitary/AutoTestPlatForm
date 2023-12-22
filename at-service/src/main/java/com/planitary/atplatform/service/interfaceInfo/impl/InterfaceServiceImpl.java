package com.planitary.atplatform.service.interfaceInfo.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.planitary.atplatform.base.commonEnum.BizCodeEnum;
import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.base.handler.PageParams;
import com.planitary.atplatform.base.utils.UniqueStringIdGenerator;
import com.planitary.atplatform.mapper.ATPlatformInterfaceInfoMapper;
import com.planitary.atplatform.mapper.ATPlatformProjectMapper;
import com.planitary.atplatform.model.dto.*;
import com.planitary.atplatform.model.po.ATPlatformInterfaceInfo;
import com.planitary.atplatform.model.po.ATPlatformProject;
import com.planitary.atplatform.service.handler.ExcelReaderHandler;
import com.planitary.atplatform.service.handler.impl.ExcelReaderHandlerImpl;
import com.planitary.atplatform.service.interfaceInfo.InterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.font.ShapeGraphicAttribute;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.service.impl
 * @name：InterfaceServiceImpl
 * @Date：2023/12/5 9:49 下午
 * @Filename：InterfaceServiceImpl
 * @description：
 */
@Service
@Slf4j
public class InterfaceServiceImpl implements InterfaceService {
    @Resource
    private ATPlatformInterfaceInfoMapper atPlatformInterfaceInfoMapper;

    @Resource
    private ATPlatformProjectMapper atPlatformProjectMapper;

    @Resource
    private UniqueStringIdGenerator uniqueStringIdGenerator;

    @Resource
    private ExcelReaderHandler excelReaderHandler;


    @Override
    @Transactional
    public Map<String, String> insertInterface(AddInterfaceDTO addInterfaceDTO) {
        // 校验项目的合法性
        LambdaQueryWrapper<ATPlatformProject> atTestProjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        atTestProjectLambdaQueryWrapper.eq(ATPlatformProject::getProjectId, addInterfaceDTO.getProjectId());
        ATPlatformProject atTestProject = atPlatformProjectMapper.selectOne(atTestProjectLambdaQueryWrapper);
        if (atTestProject == null) {
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        ATPlatformInterfaceInfo interfaceInfo = new ATPlatformInterfaceInfo();
        String interfaceId = uniqueStringIdGenerator.idGenerator();
        BeanUtils.copyProperties(addInterfaceDTO, interfaceInfo);
        interfaceInfo.setInterfaceId(interfaceId);
        interfaceInfo.setCreateUser("zane");
        interfaceInfo.setProjectId(addInterfaceDTO.getProjectId());
        interfaceInfo.setVersion(1);
        int insert = atPlatformInterfaceInfoMapper.insert(interfaceInfo);
        if (insert <= 0) {
            log.error("执行失败:{}", ExceptionEnum.INSERT_FAILED.getErrMessage());
            ATPlatformException.exceptionCast(ExceptionEnum.INSERT_FAILED);
        }
        log.debug("插入成功");
        Map<String, String> res = new HashMap<>();
        res.put("interfaceId", interfaceId);
        return res;
    }

    @Override
    public PageResult<ATPlatformInterfaceInfo> queryInterfaceInfoList(PageParams pageParams, QueryInterfaceInfoDTO queryInterfaceInfoDTO) {
        String projectId = queryInterfaceInfoDTO.getProjectId();
        final String SUCCESS_CODE = "200";
        if (projectId == null) {
            log.error("projectId不能为空");
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }
        LambdaQueryWrapper<ATPlatformProject> atPlatformProjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        atPlatformProjectLambdaQueryWrapper.eq(ATPlatformProject::getProjectId, projectId);
        ATPlatformProject atPlatformProject = atPlatformProjectMapper.selectOne(atPlatformProjectLambdaQueryWrapper);
        if (atPlatformProject == null) {
            log.error("project不存在");
            ATPlatformException.exceptionCast("接口所属项目不存在");
        }

        LambdaQueryWrapper<ATPlatformInterfaceInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ATPlatformInterfaceInfo::getProjectId, projectId)
                .eq(StringUtils.isNotEmpty(queryInterfaceInfoDTO.getInterfaceUrl()),
                        ATPlatformInterfaceInfo::getInterfaceUrl, queryInterfaceInfoDTO.getInterfaceUrl())
                .like(StringUtils.isNotEmpty(queryInterfaceInfoDTO.getInterfaceName()),
                        ATPlatformInterfaceInfo::getInterfaceName, queryInterfaceInfoDTO.getInterfaceName())
                .eq(StringUtils.isNotEmpty(queryInterfaceInfoDTO.getInterfaceUrl()),
                        ATPlatformInterfaceInfo::getInterfaceUrl, queryInterfaceInfoDTO.getInterfaceUrl());

        long pageNo = pageParams.getPageNo();
        long pageSize = pageParams.getPageSize();
        if (pageNo <= 0 || pageSize <= 0) {
            ATPlatformException.exceptionCast(ExceptionEnum.PAGINATION_PARAM_ERROR);
        }
        Page<ATPlatformInterfaceInfo> page = new Page<>(pageNo, pageSize);
        Page<ATPlatformInterfaceInfo> interfaceInfoPage = atPlatformInterfaceInfoMapper.selectPage(page, lambdaQueryWrapper);
        List<ATPlatformInterfaceInfo> records = interfaceInfoPage.getRecords();
        long total = interfaceInfoPage.getTotal();
        log.info("查询到的记录总数:{}", total);
        return new PageResult<>(records, total, pageNo, pageSize, SUCCESS_CODE);
    }

    @Override
    @Transactional
    public Map<String, String> updateInterface(String projectId, ATPlatformInterfaceInfo atPlatformInterfaceInfo) {
        boolean versionFlag = false;
        // 校验projectId合法性以及与当前interface的关联性
        LambdaQueryWrapper<ATPlatformProject> projectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        projectLambdaQueryWrapper.eq(ATPlatformProject::getProjectId, projectId);
        ATPlatformProject projectByInterface = atPlatformProjectMapper.selectOne(projectLambdaQueryWrapper);
        if (projectByInterface == null) {
            log.error("项目:{}不存在", projectId);
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        String interfaceId = atPlatformInterfaceInfo.getInterfaceId();
        LambdaQueryWrapper<ATPlatformInterfaceInfo> interfaceInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        interfaceInfoLambdaQueryWrapper.eq(ATPlatformInterfaceInfo::getInterfaceId, interfaceId);
        ATPlatformInterfaceInfo interfaceInfo = atPlatformInterfaceInfoMapper.selectOne(interfaceInfoLambdaQueryWrapper);
        if (interfaceInfo == null) {
            log.error("接口:{}不存在", interfaceId);
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        if (!Objects.equals(projectId, interfaceInfo.getProjectId())) {
            log.error("接口:{}与项目:{}不匹配", interfaceId, projectId);
            ATPlatformException.exceptionCast("接口与项目不匹配!");
        }
        // 更新版本version（version只在字段更新时更新）
        UpdateWrapper<ATPlatformInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interface_id", interfaceId);
        // 校验requestBody,使用json进行比较，不一致则发生了变更
        JSONObject originalJSON = JSON.parseObject(interfaceInfo.getRequestBody());
        JSONObject targetJSON = JSON.parseObject(atPlatformInterfaceInfo.getRequestBody());
        String originalStandardJSON = JSON.toJSONString(originalJSON);
        String targetStandardJSON = JSON.toJSONString(targetJSON);
        if (!Objects.equals(originalStandardJSON, targetStandardJSON)) {
            updateWrapper.set("request_body", atPlatformInterfaceInfo.getRequestBody());
            versionFlag = true;
        }
        if (!Objects.equals(atPlatformInterfaceInfo.getInterfaceName(), interfaceInfo.getInterfaceName())) {
            updateWrapper.set("interface_name", atPlatformInterfaceInfo.getInterfaceName());
            versionFlag = true;
        }
        if (!Objects.equals(atPlatformInterfaceInfo.getInterfaceUrl(), interfaceInfo.getInterfaceUrl())) {
            updateWrapper.set("interface_url", atPlatformInterfaceInfo.getInterfaceUrl());
            versionFlag = true;
        }
        if (!Objects.equals(atPlatformInterfaceInfo.getRemark(), interfaceInfo.getRemark())) {
            updateWrapper.set("remark", atPlatformInterfaceInfo.getRemark());
            versionFlag = true;
        }
        if (versionFlag) {
            updateWrapper.set("version", interfaceInfo.getVersion() + 1);
            updateWrapper.set("update_time", LocalDateTime.now());
            int updateCount = atPlatformInterfaceInfoMapper.update(null, updateWrapper);
            if (updateCount <= 0) {
                ATPlatformException.exceptionCast(ExceptionEnum.UPDATE_FAILED);
            }
            log.info("更新接口成功");
        } else {
            log.info("接口无变化，无需变更");
        }
        Map<String, String> resMap = new HashMap<>();
        resMap.put("interfaceId", interfaceId);
        return resMap;
    }

    /**
     * 这里的逻辑，先通过导入接口导入excel，然后用户会选择接口url，并选择需要执行的接口字段
     *
     * @param chosenParamDTO
     * @return
     */
    @Override
    public void coreFillParameter(ChosenParamDTO chosenParamDTO) {
        // 解析外层key（interfaceUrl）
        Map<String, List<ParamDTO>> chosenParamMap = chosenParamDTO.getChosenParamDTO();
        if (!chosenParamMap.isEmpty()) {
            for (Map.Entry<String, List<ParamDTO>> outerEntry : chosenParamMap.entrySet()) {
                String interfaceUrl = outerEntry.getKey();
                ATPlatformInterfaceInfo atPlatformInterfaceInfo = atPlatformInterfaceInfoMapper.
                        selectOne(new LambdaQueryWrapper<ATPlatformInterfaceInfo>()
                                .eq(ATPlatformInterfaceInfo::getInterfaceUrl, interfaceUrl));
                if (atPlatformInterfaceInfo == null) {
                    ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
                }
                // 解析内层map(封装了参数的map，key是参数名，value是值)，该map放在list中
                List<ParamDTO> paramDTOList = outerEntry.getValue();
                // TODO: 2023/12/23 取出param填充到接口中发起调用
            }
        }

    }
//        return null;
}
