package com.planitary.atplatform.service.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.base.handler.CommonHttpPost;
import com.planitary.atplatform.base.utils.GeneralIdGenerator;
import com.planitary.atplatform.mapper.ATPlatformInterfaceCallRecordMapper;
import com.planitary.atplatform.mapper.ATPlatformProjectMapper;
import com.planitary.atplatform.model.dto.ExecuteDTO;
import com.planitary.atplatform.model.dto.ExecuteResponseDTO;
import com.planitary.atplatform.model.po.ATPlatformInterfaceCallRecord;
import com.planitary.atplatform.model.po.ATPlatformProject;
import com.planitary.atplatform.service.ExecuteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.time.*;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class ExecuteHandlerImpl implements ExecuteHandler {
    private static final String LOGIN_AUTHORIZED_HEADER = "Basic YXBwOmFwcA==";

    private static String TEST_ENV_PREFIX = "https://test-scs-login.52imile.cn/";
    private static String LOCAL_ENV_PREFIX = "http://localhost:8080/";

    @Resource
    CommonHttpPost commonHttpPost;

    @Resource
    PlatformTransactionManager transactionManager;

    @Resource
    ATPlatformProjectMapper atPlatformProjectMapper;

    @Resource
    ATPlatformInterfaceCallRecordMapper atPlatformInterfaceCallRecordMapper;

    /**
     * 接口执行类
     * @param executeDTO            执行参数
     * @return
     */
    @Override
    @Transactional
    public ExecuteResponseDTO doInterfaceExecutor(ExecuteDTO executeDTO) {
        if (executeDTO == null){
            log.error("{}", ExceptionEnum.OBJECT_NULL);
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        String contentType = "application/json";
        Map<String, String> requestBody = executeDTO.getRequestBody();
        LambdaQueryWrapper<ATPlatformProject> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ATPlatformProject::getProjectId,executeDTO.getProjectId());
        ATPlatformProject atTestProject = atPlatformProjectMapper.selectOne(lambdaQueryWrapper);
        if (atTestProject == null){
            log.error("{}", ExceptionEnum.OBJECT_NULL);
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        String projectName = atTestProject.getProjectUrl();
//        String url = TEST_ENV_PREFIX + projectName + "/" + executeDTO.getInterfaceUrl();
        // 本地测试用地址
        String url = LOCAL_ENV_PREFIX + executeDTO.getInterfaceUrl();
        log.info("请求地址:{}",url);
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", LOGIN_AUTHORIZED_HEADER);
        headers.put("Content-Type",contentType);

        long requireTime = executeDTO.getRequireTime();

        // 公共调用方法
        String executeJson = commonHttpPost.doCommonHttpPostJson(requestBody, headers, url);
        LocalDateTime executeTime = LocalDateTime.now();
        long exeTimeStamp = this.convertLocalDateTime2TimeStamp(executeTime);
        log.info("resbody:{}",executeJson);

        // 插入接口调用记录表
        ATPlatformInterfaceCallRecord atTestInterfaceCallRecord = new ATPlatformInterfaceCallRecord();
        atTestInterfaceCallRecord.setInterfaceId(executeDTO.getInterfaceId());
        atTestInterfaceCallRecord.setRecordId(GeneralIdGenerator.generateId());
        atTestInterfaceCallRecord.setExecuteTime(executeTime);
        atTestInterfaceCallRecord.setDurationTime(exeTimeStamp - requireTime);
        String resBody = JSON.toJSONString(executeDTO);
        atTestInterfaceCallRecord.setRequestBody(resBody);
        atTestInterfaceCallRecord.setResponseBody(executeJson);
        int insert = atPlatformInterfaceCallRecordMapper.insert(atTestInterfaceCallRecord);
        if (insert <= 0){
            ATPlatformException.exceptionCast(ExceptionEnum.INSERT_FAILED);
        }
        log.debug("插入成功,接口id:{}",executeDTO.getInterfaceId());

        // 封装返回数据
        ExecuteResponseDTO executeResponseDTO = new ExecuteResponseDTO();
        BeanUtils.copyProperties(atTestInterfaceCallRecord,executeResponseDTO);
        return executeResponseDTO;
    }

    private long convertLocalDateTime2TimeStamp(LocalDateTime localDateTime){
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
        return zonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * 毫秒级时间戳转换为日期
     * @param timeStamp     毫秒级时间戳
     * @return
     */
    private LocalDateTime convertTimeStamp2LocalDateTime(Long timeStamp){
        Instant instant = Instant.ofEpochMilli(timeStamp);
        return instant.atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
    }
}
