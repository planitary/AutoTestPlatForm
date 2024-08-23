package com.planitary.atplatform.service.caseSet.impl;

import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.base.utils.GeneralIdGenerator;
import com.planitary.atplatform.base.utils.UniqueStringIdGenerator;
import com.planitary.atplatform.mapper.tcsProgress.ATPlatformTCSProgressMapper;
import com.planitary.atplatform.model.dto.AddTCSProgressDTO;
import com.planitary.atplatform.model.dto.TCSProgressAsserts;
import com.planitary.atplatform.model.po.ATPlatformTCSProgress;
import com.planitary.atplatform.model.po.ATPlatformTCSStep;
import com.planitary.atplatform.service.caseSet.CaseSetProgressService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.sl.draw.geom.AddDivideExpression;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public CaseSetProgressServiceImpl(UniqueStringIdGenerator uniqueStringIdGenerator,
                                      ATPlatformTCSProgressMapper atPlatformTCSProgressMapper){
        this.uniqueStringIdGenerator = uniqueStringIdGenerator;
        this.atPlatformTCSProgressMapper = atPlatformTCSProgressMapper;
    }


    @Override
    public String addTCSProgress(List<AddTCSProgressDTO> addTCSProgressList) {
        if (addTCSProgressList == null){
            log.error("数据为空");
            ATPlatformException.exceptionCast(ExceptionEnum.BIZ_ERROR);
        }
        for (AddTCSProgressDTO addTCSProgressDTO : addTCSProgressList){
            if (addTCSProgressDTO.getStepName() == null) {
                log.error("stepName为空");
                ATPlatformException.exceptionCast(ExceptionEnum.BIZ_ERROR);
            }
            else {
                log.info("解析到当前progress:{}",addTCSProgressDTO);
                ATPlatformTCSStep atPlatformTCSStep = new ATPlatformTCSStep();
//                TCSProgressAsserts tcsProgressAsserts = new TCSProgressAsserts();
                BeanUtils.copyProperties(addTCSProgressDTO,atPlatformTCSStep);
                String stepId = "6" + uniqueStringIdGenerator.idGenerator();
                atPlatformTCSStep.setStepId(stepId);
                int insert = atPlatformTCSProgressMapper.insert(atPlatformTCSStep);
                if (insert <= 0) {
                    log.error("执行失败:{}", ExceptionEnum.INSERT_FAILED.getErrMessage());
                    ATPlatformException.exceptionCast(ExceptionEnum.INSERT_FAILED);
                }
                return stepId;
            }
        }
        return null;
    }
}
