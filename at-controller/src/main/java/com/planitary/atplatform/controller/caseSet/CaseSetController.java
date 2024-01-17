package com.planitary.atplatform.controller.caseSet;

import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.customResult.PtResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.base.handler.PageParams;
import com.planitary.atplatform.model.dto.AddCaseSetDTO;
import com.planitary.atplatform.model.dto.QueryCaseSetListDTO;
import com.planitary.atplatform.model.po.ATPlatformCaseSet;
import com.planitary.atplatform.service.caseSet.CaseSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.controller
 * @name：caseSetController
 * @Date：2023/12/14 10:57 下午
 * @Filename：caseSetController
 * @description：
 */
@RestController
@Slf4j
public class CaseSetController {

    @Resource
    CaseSetService caseSetService;

    @PostMapping("/caseSet/insertCaseSet")
    public PtResult<String> insertCaseSet(@RequestBody AddCaseSetDTO addCaseSetDTO){
        if (addCaseSetDTO == null){
            log.error("参数为空!");
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }
        String setId = caseSetService.addCaseSet(addCaseSetDTO);
        return PtResult.success(setId);
    }

    @PostMapping("/caseSet/updateCaseSet")
    public PtResult<String> updateCaseSet(String projectId,@RequestBody AddCaseSetDTO addCaseSetDTO){
        if (projectId == null){
            log.error("项目id为空");
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }
        String caseSetId = caseSetService.updateCaseSet(addCaseSetDTO);
        return PtResult.success(caseSetId);
    }

    @GetMapping("/caseSet/caseSetList")
    public PageResult<ATPlatformCaseSet> getCaseSetList(PageParams pageParams, @RequestBody QueryCaseSetListDTO queryCaseSetListDTO){
        return caseSetService.queryCaseSetList(pageParams,queryCaseSetListDTO);
    }
}
