package com.planitary.atplatform.controller.caseSet;

import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PtResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.model.dto.AddTCSProgressDTO;
import com.planitary.atplatform.model.dto.AddTCSProgressInfoDTO;
import com.planitary.atplatform.service.caseSet.CaseSetProgressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class CaseSetProgressController {

    private final CaseSetProgressService caseSetProgressService;
    public CaseSetProgressController(CaseSetProgressService caseSetProgressService){
        this.caseSetProgressService = caseSetProgressService;
    }

    @PostMapping("/caseset/progress/addProgress")
    public PtResult<String> addProgress(@RequestBody AddTCSProgressDTO addTCSProgressDTO){
        if (addTCSProgressDTO == null){
            log.error("详情为空!");
            ATPlatformException.exceptionCast(ExceptionEnum.BIZ_ERROR);
        }
        if (addTCSProgressDTO.getProjectDTO() == null){
            log.error("项目信息不存在!");
            ATPlatformException.exceptionCast(ExceptionEnum.BIZ_ERROR);
        }
        String stepId = caseSetProgressService.addTCSProgress(addTCSProgressDTO);
        return PtResult.success(stepId);
    }
}

