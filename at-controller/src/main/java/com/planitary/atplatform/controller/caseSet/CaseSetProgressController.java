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

    // TODO: 2024/8/27 部分值无法映射出来 
    @PostMapping("/caseset/progress/addProgress")
    public PtResult<List<String>> addProgress(@RequestBody AddTCSProgressDTO addTCSProgressDTO){
        if (addTCSProgressDTO == null){
            log.error("详情为空!");
            ATPlatformException.exceptionCast(ExceptionEnum.BIZ_ERROR);
        }
        if (addTCSProgressDTO.getProjectInfo() == null){
            log.error("项目不存在!");
            ATPlatformException.exceptionCast(ExceptionEnum.BIZ_ERROR);
        }
        List<String> stepIds = caseSetProgressService.addTCSProgress(addTCSProgressDTO);
        return PtResult.success(stepIds);
    }
}

