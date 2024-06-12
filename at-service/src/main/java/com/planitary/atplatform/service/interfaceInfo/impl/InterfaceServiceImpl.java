package com.planitary.atplatform.service.interfaceInfo.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.model.po.PageParams;
import com.planitary.atplatform.base.utils.UniqueStringIdGenerator;
import com.planitary.atplatform.mapper.ATPlatformInterfaceInfoMapper;
import com.planitary.atplatform.mapper.ATPlatformProjectMapper;
import com.planitary.atplatform.model.dto.*;
import com.planitary.atplatform.model.po.ATPlatformInterfaceInfo;
import com.planitary.atplatform.model.po.ATPlatformProject;
import com.planitary.atplatform.service.handler.ExecuteHandler;
import com.planitary.atplatform.service.interfaceInfo.InterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
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
import java.util.concurrent.atomic.AtomicInteger;

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
    private ExecuteHandler executeHandler;

    private final Integer THREAD_POOL_SIZE = 2;

    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE); // 线程池大小
    private final List<CompletableFuture<Map<String, Object>>> dataPool = new CopyOnWriteArrayList<>();


    @Override
    @Transactional
    public Map<String, String> insertInterface(AddInterfaceDTO addInterfaceDTO) {
        // 校验项目的合法性
        LambdaQueryWrapper<ATPlatformProject> atTestProjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        atTestProjectLambdaQueryWrapper.eq((StringUtils.isNotEmpty(addInterfaceDTO.getProjectId())),
                        ATPlatformProject::getProjectId, addInterfaceDTO.getProjectId())
                .eq((StringUtils.isNotEmpty(addInterfaceDTO.getProjectName())),
                        ATPlatformProject::getProjectName, addInterfaceDTO.getProjectName());
        ATPlatformProject atTestProject = atPlatformProjectMapper.selectOne(atTestProjectLambdaQueryWrapper);
        if (atTestProject == null) {
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        ATPlatformInterfaceInfo interfaceInfo = new ATPlatformInterfaceInfo();
        String interfaceId = uniqueStringIdGenerator.idGenerator();
        BeanUtils.copyProperties(addInterfaceDTO, interfaceInfo);
        // 处理没有传projectId的情况
        if (addInterfaceDTO.getProjectId() == null || Objects.equals(addInterfaceDTO.getProjectId(), "")) {
            interfaceInfo.setProjectId(atTestProject.getProjectId());
        }
        interfaceInfo.setInterfaceId(interfaceId);
        interfaceInfo.setCreateUser("zane");
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
                        ATPlatformInterfaceInfo::getInterfaceName, queryInterfaceInfoDTO.getInterfaceName());

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

    // TODO: 2024/4/25 这里的sql需要额外增加其他条件的查询，不能只有in，需要手动sql完成
    @Override
    public PageResult<InterfaceWithProjectDTO> queryInterfaceInfoList(QueryInterfaceDTO queryInterfaceDTO) {
        String projectId = queryInterfaceDTO.getProjectId();
        final String SUCCESS_CODE = "200";

        if (!Objects.equals(queryInterfaceDTO.getProjectId(), "") && queryInterfaceDTO.getProjectId() != null) {
            LambdaQueryWrapper<ATPlatformProject> atPlatformProjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            atPlatformProjectLambdaQueryWrapper.eq(ATPlatformProject::getProjectId, projectId);
            ATPlatformProject atPlatformProject = atPlatformProjectMapper.selectOne(atPlatformProjectLambdaQueryWrapper);
            if (atPlatformProject == null) {
                log.error("project不存在");
                ATPlatformException.exceptionCast("接口所属项目不存在");
            }
        }

        long pageNo = queryInterfaceDTO.getPageNo();
        long pageSize = queryInterfaceDTO.getPageSize();
        if (pageNo <= 0 || pageSize <= 0) {
            ATPlatformException.exceptionCast(ExceptionEnum.PAGINATION_PARAM_ERROR);
        }
        Page<InterfaceWithProjectDTO> page = new Page<>(pageNo, pageSize);
        Page<InterfaceWithProjectDTO> interfaceInfoPage = atPlatformInterfaceInfoMapper.getInterfaceWithProject(page, queryInterfaceDTO);
        List<InterfaceWithProjectDTO> records = interfaceInfoPage.getRecords();
        long total = interfaceInfoPage.getTotal();
        log.info("查询到的记录总数:{}", total);
        return new PageResult<>(records, total, pageNo, pageSize, SUCCESS_CODE);
    }

    @Override
    public ATPlatformInterfaceInfo getInterfaceDetail(BaseInterfaceDTO baseInterfaceDTO) {
        String interfaceId = baseInterfaceDTO.getInterfaceId();
        if (interfaceId == null || interfaceId.equals("")) {
            log.error("接口id不能为空");
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }
        LambdaQueryWrapper<ATPlatformInterfaceInfo> interfaceInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        interfaceInfoLambdaQueryWrapper.eq(ATPlatformInterfaceInfo::getInterfaceId, interfaceId);
        ATPlatformInterfaceInfo atPlatformInterfaceInfo = atPlatformInterfaceInfoMapper.selectOne(interfaceInfoLambdaQueryWrapper);
        if (atPlatformInterfaceInfo == null) {
            log.error("项目不存在");
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        return atPlatformInterfaceInfo;
    }

    @Override
    public List<ATPlatformInterfaceInfo> getInterfaceDetailByName(BaseInterfaceDTO baseInterfaceDTO) {
        String interfaceName = baseInterfaceDTO.getInterfaceName();
        LambdaQueryWrapper<ATPlatformInterfaceInfo> interfaceInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        interfaceInfoLambdaQueryWrapper
                .eq((StringUtils.isNotEmpty(baseInterfaceDTO.getProjectId())),ATPlatformInterfaceInfo::getProjectId, baseInterfaceDTO.getProjectId())
                .like(ATPlatformInterfaceInfo::getInterfaceName, interfaceName);
        return atPlatformInterfaceInfoMapper.selectList(interfaceInfoLambdaQueryWrapper);
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

    @Override
    public Map<String, String> updateInterfaceV2(ATPlatformInterfaceInfo atPlatformInterfaceInfo) {
        boolean versionFlag = false;
        String projectId = atPlatformInterfaceInfo.getProjectId();
        // 校验projectId合法性以及与当前interface的关联性
        LambdaQueryWrapper<ATPlatformProject> projectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        projectLambdaQueryWrapper.eq(ATPlatformProject::getProjectId, projectId);
        ATPlatformProject projectByInterface = atPlatformProjectMapper.selectOne(projectLambdaQueryWrapper);
        if (projectByInterface == null) {
            log.error("项目:{}不存在", projectId);
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        if (projectByInterface.getIsDelete() == 1) {
            log.error("项目:{}已删除", projectId);
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
        if (!Objects.equals(atPlatformInterfaceInfo.getRequestBody(), interfaceInfo.getRequestBody())) {
            updateWrapper.set("request_body", atPlatformInterfaceInfo.getRequestBody());
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
        resMap.put("status", "success");
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
        String traceId = MDC.get("traceId");
        List<CompletableFuture<Map<String, Object>>> futures = new ArrayList<>();
        List<InterfaceParamDTO> interfaceParamDTOS = chosenParamDTO.getChosenParamDTOs();

//        // 清空数据池，准备接收最新的数据
        dataPool.clear();

        if (interfaceParamDTOS == null || interfaceParamDTOS.isEmpty()) {
            log.debug("无数据");
        } else {
            log.info("=====开始执行异步任务=====");
            for (InterfaceParamDTO interfaceParamDTO : interfaceParamDTOS) {
                String interfaceUrl = interfaceParamDTO.getInterfaceUrl();
                ATPlatformInterfaceInfo atPlatformInterfaceInfo = atPlatformInterfaceInfoMapper.selectOne(
                        new LambdaQueryWrapper<ATPlatformInterfaceInfo>()
                                .eq(ATPlatformInterfaceInfo::getInterfaceUrl, interfaceUrl));

                if (atPlatformInterfaceInfo == null) {
                    ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
                }

                // 遍历list，一组key-value是一个请求的参数
                List<Map<String, Object>> paramDTOList = interfaceParamDTO.getInterfaceParamDTOs();
                for (Map<String, Object> paramDTO : paramDTOList) {
                    // 初始化currentRequestBody
                    Map<String, Object> currentRequestBody = new HashMap<>();
                    CompletableFuture<Map<String, Object>> future = CompletableFuture.supplyAsync(() -> {
                        MDC.put("traceId", traceId);
                        try {
                            currentRequestBody.put("param", paramDTO);
                            // 添加额外的参数
                            currentRequestBody.put("interfaceInfo", atPlatformInterfaceInfo);

                            Thread.sleep(1000);
                            // 返回解析结果
                            log.info("解析成功: threadId:{}-{}", Thread.currentThread(), currentRequestBody);
                            return currentRequestBody;

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
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApplyAsync(ignored -> {
//                    // 等待所有解析完成后，清空线程池中旧的数据
//                    dataPool.clear();
                    log.debug("==========所有解析完成=============");
                    return "所有解析完成";
                });
    }


    @Override
    @Async
    public CompletableFuture<Void> coreExecutor(String callableMethodCode) {
        String traceId = MDC.get("traceId");
        int totalTasks = dataPool.size();
        AtomicInteger completedTasks = new AtomicInteger(0);

        for (CompletableFuture<Map<String, Object>> future : dataPool) {
            ExecutorService newExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            future.thenAcceptAsync(data -> {
                MDC.put("traceId", traceId);
                try {
                    // 业务执行逻辑(更新接口请求体)
                    if (Objects.equals(callableMethodCode, "CM001")) {
                        log.info("消费到参数:{}", data);
                        ATPlatformInterfaceInfo interfaceInfo = (ATPlatformInterfaceInfo) data.get("interfaceInfo");
                        if (interfaceInfo == null) {
                            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
                        }
                        log.info("拿到生产的接口id:{}", interfaceInfo.getInterfaceId());
                        // 解析参数，转为json
                        String paramJson = JSON.toJSONString(data.get("param"));
                        log.debug("开始消费，参数:{}", paramJson);

                        String requestBody = interfaceInfo.getRequestBody();
                        Map<String, Object> interfaceRequestBody = JSON.parseObject(requestBody);
                        this.updateRequestBody(JSON.parseObject(paramJson), interfaceRequestBody);
                        // 重新序列化为json
                        String afterRequestBody = JSON.toJSONString(interfaceRequestBody);
                        // 更新interfaceInfo的requestBody字段
                        try {
                            this.updateInterfaceInfo("request_body", afterRequestBody,
                                    "interface_id", interfaceInfo.getInterfaceId());
                            log.info("更新完成");

                        } catch (ATPlatformException e) {
                            e.printStackTrace();
                            log.error("业务异常:{}", e.getMessage());
                        }
                    }
                    // 业务执行逻辑(外部调用)
                    if (Objects.equals(callableMethodCode, "CM002")) {
                        ATPlatformInterfaceInfo atPlatformInterfaceInfo = (ATPlatformInterfaceInfo) data.get("interfaceInfo");
                        if (atPlatformInterfaceInfo == null) {
                            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
                        }
                        log.info("拿到生产的接口id:{}", atPlatformInterfaceInfo.getInterfaceId());
                        // 解析参数，转为json
                        String paramJson = JSON.toJSONString(data.get("param"));
                        String requestBody = atPlatformInterfaceInfo.getRequestBody();
                        Map<String, Object> requestBodyMap = JSON.parseObject(requestBody);
                        ExecuteDTO executeDTO = new ExecuteDTO();
                        executeDTO.setProjectId(atPlatformInterfaceInfo.getProjectId());
                        executeDTO.setInterfaceUrl(atPlatformInterfaceInfo.getInterfaceUrl());
                        executeDTO.setInterfaceId(atPlatformInterfaceInfo.getInterfaceId());
                        try {
                            // 填充参数化请求后，连同公共字段一同发起请求
                            this.updateRequestBody(JSON.parseObject(paramJson), requestBodyMap);
                            executeDTO.setRequestBody(requestBodyMap);
                            // 调用CommonHtpPost方法发起公共调用
                            executeDTO.setRequireTime(System.currentTimeMillis());
                            ExecuteResponseDTO executeResponseDTO = executeHandler.doInterfaceExecutor(executeDTO);
                            log.debug("调用结束,调用结果:{}", executeResponseDTO);
                        } catch (ATPlatformException e) {
                            e.printStackTrace();
                            log.error("业务异常:{}", e.getMessage());
                        }
                    }
                    Thread.sleep(1500); // 模拟延迟，请注意线程睡眠的使用
                } catch (InterruptedException e) {
                    log.error("业务执行异常: {}", e.getMessage());
                    throw new RuntimeException(e);
                } finally {
                    int completed = completedTasks.incrementAndGet();
                    if (completed == totalTasks) {
                        // 当所有任务完成时清空线程池
                        newExecutorService.shutdown();
                        log.debug("==========异步任务执行完毕=============");
                    }
                }
            }, newExecutorService);
        }
        return CompletableFuture.completedFuture(null);
    }


    /**
     * 更新接口参数，因为interfaceInfo的requestBody字段也是一个json
     *
     * @param sourceMap 实际传参
     * @param targetMap 接口入参（要更新的）
     */
    protected void updateRequestBody(Map<String, Object> sourceMap, Map<String, Object> targetMap) {
        log.info("接收到的参数为:{},接口的参数为:{}", sourceMap, targetMap);
        if (sourceMap.isEmpty() || targetMap.isEmpty()) {
            log.info("接口参数或传参为空，无需变更");
        } else {
            for (Map.Entry<String, Object> entryA : sourceMap.entrySet()) {
                if (targetMap.containsKey(entryA.getKey())) {
                    targetMap.put(entryA.getKey(), entryA.getValue());
                }
            }
        }
    }

    @Transactional
    public void updateInterfaceInfo(String key, Object value, String primaryKey, Object primaryValue) {
        UpdateWrapper<ATPlatformInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(key, value).set("update_time", LocalDateTime.now()).eq(primaryKey, primaryValue);
        int update = atPlatformInterfaceInfoMapper.update(null, updateWrapper);
        if (update <= 0) {
            ATPlatformException.exceptionCast(ExceptionEnum.UPDATE_FAILED);
        }
    }


}
