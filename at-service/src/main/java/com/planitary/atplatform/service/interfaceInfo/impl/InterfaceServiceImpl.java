package com.planitary.atplatform.service.interfaceInfo.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.planitary.atplatform.service.interfaceInfo.InterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // 线程池大小
    private final List<CompletableFuture<List<Map<String, Object>>>> dataPool = new CopyOnWriteArrayList<CompletableFuture<List<Map<String, Object>>>>();


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
     * 异步解析封装数据
     * 这里的逻辑，先通过导入接口导入excel，然后用户会选择接口url，并选择需要执行的接口字段
     *
     * @param chosenParamDTO 用户选择的excel解析值（包含接口url和传参）
     * @return
     */
    @Override
    @Async
    public CompletableFuture<String> coreFillParameter(ChosenParamDTO chosenParamDTO) {
        List<CompletableFuture<List<Map<String, Object>>>> futures = new ArrayList<>();
        List<InterfaceParamDTO> interfaceParamDTOS = chosenParamDTO.getChosenParamDTOs();
        if (interfaceParamDTOS == null || interfaceParamDTOS.size() == 0){
            log.debug("无数据");
        }
        else {
            for (InterfaceParamDTO interfaceParamDTO : interfaceParamDTOS) {
                CompletableFuture<List<Map<String, Object>>> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        String interfaceUrl = interfaceParamDTO.getInterfaceUrl();
                        ATPlatformInterfaceInfo atPlatformInterfaceInfo = atPlatformInterfaceInfoMapper.selectOne(
                                new LambdaQueryWrapper<ATPlatformInterfaceInfo>()
                                        .eq(ATPlatformInterfaceInfo::getInterfaceUrl, interfaceUrl));

                        if (atPlatformInterfaceInfo == null) {
                            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
                        }

                        // 初始化currentRequestBody
                        Map<String, Object> currentRequestBody;
                        List<Map<String,Object>> requestBodies = new ArrayList<>();

                        // 遍历list，一组key-value是一个请求的参数
                        List<ParamDTO> paramDTOList = interfaceParamDTO.getInterfaceParamDTOs();
                        for (ParamDTO paramDTO : paramDTOList) {
                            currentRequestBody = paramDTO.getParams();
                            currentRequestBody.put("interfaceId", atPlatformInterfaceInfo.getInterfaceId());
                            requestBodies.add(currentRequestBody);
                        }

                        // 添加额外的参数

                        Thread.sleep(1000);
                        // 返回解析结果
                        log.info("解析成功: threadId:{}-{}", Thread.currentThread(), requestBodies);
                        return requestBodies;
                    } catch (InterruptedException e) {
                        log.error("业务异常: {}", e.getMessage());
                        throw new RuntimeException(e.getMessage());
                    }
                }, executorService);

                // 解析出的结果放入数据池
                dataPool.add(future);
                futures.add(future);
            }
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApplyAsync(ignored -> "所有解析完成");
    }

    @Override
    @Async
    public CompletableFuture<Void> coreExecutor() {
        for (CompletableFuture<List<Map<String, Object>>> future : dataPool) {
            future.thenAcceptAsync(data -> {
                // 业务执行核心逻辑(填充interface的requestBody并发起调用
                try {
                    System.out.println(data);
                    // 实际业务逻辑，注意线程睡眠的使用
                    // 填充interface的requestBody并发起调用
                    // ...

                    Thread.sleep(1500); // 仅用于模拟延迟，请谨慎使用线程睡眠

                } catch (InterruptedException e) {
                    log.error("业务执行异常: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            }, executorService);
        }
        return CompletableFuture.completedFuture(null);
    }


//    public static void main(String[] args) {
//        InterfaceServiceImpl interfaceService = new InterfaceServiceImpl();
//        InterfaceParamDTO interfaceParamDTO = new InterfaceParamDTO();
//        ParamDTO paramDTO1 = new ParamDTO();
//        ParamDTO paramDTO2 = new ParamDTO();
//
//        Map<String,Object> map1 = new HashMap<>();
//        map1.put("id",1);
//        map1.put("name","jack");
//        map1.put("age",20);
//        Map<String,Object> map2 = new HashMap<>();
//        map2.put("id",2);
//        map2.put("name","peter");
//        map2.put("age",35);
//        paramDTO1.setParamsMap(map1);
//        paramDTO2.setParamsMap(map2);
//        List<ParamDTO> paramDTOList = new ArrayList<>();
//        paramDTOList.add(paramDTO1);
//        paramDTOList.add(paramDTO2);
//        Map<String,List<ParamDTO>> stringListMap = new HashMap<>();
//        stringListMap.put("cornerstone/test",paramDTOList);
//        interfaceParamDTO.setChosenParamDTO(stringListMap);
//        CompletableFuture<String> future = interfaceService.coreFillParameter(interfaceParamDTO);
//        System.out.println(future);
//        interfaceService.coreExecutor();
//    }
}
