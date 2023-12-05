package com.planitary.atplatform.service.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PtResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.base.handler.CommonHttpPost;
import com.planitary.atplatform.base.handler.PreMockLogin;
import com.planitary.atplatform.base.utils.GeneralIdGenerator;
import com.planitary.atplatform.base.utils.UniqueStringIdGenerator;
import com.planitary.atplatform.mapper.ATTestInterfaceCallRecordMapper;
import com.planitary.atplatform.mapper.ATTestProjectMapper;
import com.planitary.atplatform.model.dto.ExecuteDTO;
import com.planitary.atplatform.model.dto.ExecuteResponseDTO;
import com.planitary.atplatform.model.po.ATTestInterfaceCallRecord;
import com.planitary.atplatform.model.po.ATTestProject;
import com.planitary.atplatform.service.ExecuteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ObjectStreamClass;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class ExecuteHandlerImpl implements ExecuteHandler {
    private static final String LOGIN_AUTHORIZED_HEADER = "Basic YXBwOmFwcA==";

    private static String TEST_ENV_PREFIX = "https://test-scs-login.52imile.cn/";

    @Resource
    CommonHttpPost commonHttpPost;

    @Resource
    ATTestProjectMapper atTestProjectMapper;

    @Resource
    ATTestInterfaceCallRecordMapper atTestInterfaceCallRecordMapper;

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
        ATTestProject atTestProject = atTestProjectMapper.selectById(executeDTO.getProjectId());
        if (atTestProject == null){
            log.error("{}", ExceptionEnum.OBJECT_NULL);
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        String projectName = atTestProject.getProjectUrl();
        String url = TEST_ENV_PREFIX + projectName + "/" + executeDTO.getInterfaceUrl();
        log.info("请求地址:{}",url);
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization", LOGIN_AUTHORIZED_HEADER);
        headers.put("Content-Type",contentType);

        long requireTime = executeDTO.getRequireTime();

        // 公共调用方法
        String executeJson = commonHttpPost.doCommonHttpPostJson(requestBody, headers, url);
        long executeTime = System.currentTimeMillis();
        log.info("resbody:{}",executeJson);

        // 插入接口调用记录表
        ATTestInterfaceCallRecord atTestInterfaceCallRecord = new ATTestInterfaceCallRecord();
        atTestInterfaceCallRecord.setInterfaceId(executeDTO.getInterfaceId());
        atTestInterfaceCallRecord.setRecordId(GeneralIdGenerator.generateId());
        atTestInterfaceCallRecord.setExecuteTime(executeTime);
        atTestInterfaceCallRecord.setDurationTime(executeTime - requireTime);
        String resBody = JSON.toJSONString(executeDTO.getRequestBody());
        atTestInterfaceCallRecord.setRequestBody(resBody);
        atTestInterfaceCallRecord.setResponseBody(executeJson);
        int insert = atTestInterfaceCallRecordMapper.insert(atTestInterfaceCallRecord);
        if (insert <= 0){
            ATPlatformException.exceptionCast(ExceptionEnum.INSERT_FAILED);
        }
        log.debug("插入成功,接口id:{}",executeDTO.getInterfaceId());

        // 封装返回数据
        ExecuteResponseDTO executeResponseDTO = new ExecuteResponseDTO();
        BeanUtils.copyProperties(atTestInterfaceCallRecord,executeResponseDTO);
        return executeResponseDTO;
    }
}
