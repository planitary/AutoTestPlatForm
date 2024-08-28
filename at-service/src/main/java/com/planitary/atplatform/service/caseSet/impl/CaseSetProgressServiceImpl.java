package com.planitary.atplatform.service.caseSet.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.planitary.atplatform.base.commonEnum.BizCodeEnum;
import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.base.utils.UniqueStringIdGenerator;
import com.planitary.atplatform.mapper.project.ATPlatformProjectMapper;
import com.planitary.atplatform.mapper.tcsProgress.ATPlatformTCSProgressMapper;
import com.planitary.atplatform.model.dto.AddTCSProgressDTO;
import com.planitary.atplatform.model.dto.AddTCSProgressInfoDTO;
import com.planitary.atplatform.model.po.ATPlatformProject;
import com.planitary.atplatform.model.po.ATPlatformTCSStep;
import com.planitary.atplatform.service.caseSet.CaseSetProgressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.service.caseSet.impl
 * @name：CaseSetServiceImpl
 * @Date：2024/08/23 16:36 下午
 * @Filename：CaseSetServiceImpl
 * @description：
 */
@Service
@Slf4j
public class CaseSetProgressServiceImpl implements CaseSetProgressService {
    private final UniqueStringIdGenerator uniqueStringIdGenerator;

    private final ATPlatformTCSProgressMapper atPlatformTCSProgressMapper;

    private final ATPlatformProjectMapper atPlatformProjectMapper;

    public CaseSetProgressServiceImpl(UniqueStringIdGenerator uniqueStringIdGenerator,
                                      ATPlatformTCSProgressMapper atPlatformTCSProgressMapper,
                                      ATPlatformProjectMapper atPlatformProjectMapper){
        this.uniqueStringIdGenerator = uniqueStringIdGenerator;
        this.atPlatformTCSProgressMapper = atPlatformTCSProgressMapper;
        this.atPlatformProjectMapper = atPlatformProjectMapper;
    }


    @Override
    public List<String> addTCSProgress(AddTCSProgressDTO addTCSProgressDTO) throws IllegalAccessException {
        if (addTCSProgressDTO.getSetId() == null){
            log.error("集合用例不存在");
            ATPlatformException.exceptionCast(ExceptionEnum.BIZ_ERROR);
        }

        if (addTCSProgressDTO.getProgressList() == null){
            log.error("数据为空");
            ATPlatformException.exceptionCast(ExceptionEnum.BIZ_ERROR);
        }
//        LambdaQueryWrapper<ATPlatformProject> projectLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        projectLambdaQueryWrapper.eq(ATPlatformProject::getProjectId,addTCSProgressDTO.getProjectDTO());

        List<AddTCSProgressInfoDTO> addTCSProgressList = addTCSProgressDTO.getProgressList();
        List<String> progressIds = new ArrayList<>();
        for (AddTCSProgressInfoDTO addTCSProgressInfoDTO : addTCSProgressList){
            if (addTCSProgressInfoDTO.getStepName() == null) {
                log.error("stepName为空");
                ATPlatformException.exceptionCast(ExceptionEnum.BIZ_ERROR);
            }
            else {
                log.info("解析到当前progress:{}", addTCSProgressInfoDTO);
                ATPlatformTCSStep atPlatformTCSStep = new ATPlatformTCSStep();
//                TCSProgressAsserts tcsProgressAsserts = new TCSProgressAsserts();
                BeanUtils.copyProperties(addTCSProgressInfoDTO,atPlatformTCSStep);
                String stepId = "51" + uniqueStringIdGenerator.idGenerator();

                atPlatformTCSStep.setCaseId(addTCSProgressDTO.getSetId());
                atPlatformTCSStep.setStepId(stepId);
                atPlatformTCSStep.setDbContent(addTCSProgressInfoDTO.getDbContent());
                // 拿到对应的extraValueDesc
                String bizMsgByCode = BizCodeEnum.getBizMsgByCode(addTCSProgressInfoDTO.getExtraType());
                atPlatformTCSStep.setExtraDesc(bizMsgByCode);

                int insert = atPlatformTCSProgressMapper.insert(atPlatformTCSStep);
                if (insert <= 0) {
                    log.error("执行失败:{}", ExceptionEnum.INSERT_FAILED.getErrMessage());
                    ATPlatformException.exceptionCast(ExceptionEnum.INSERT_FAILED);
                }
                progressIds.add(stepId);
            }
        }
        return progressIds;
    }
}
