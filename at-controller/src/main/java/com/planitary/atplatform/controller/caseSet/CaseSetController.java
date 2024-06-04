package com.planitary.atplatform.controller.caseSet;

import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.customResult.PtResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.model.dto.*;
import com.planitary.atplatform.model.po.PageParams;
import com.planitary.atplatform.model.po.ATPlatformCaseSet;
import com.planitary.atplatform.service.caseSet.CaseSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
    public PtResult<String> insertCaseSet(@RequestBody AddCaseSetDTO addCaseSetDTO) {
        if (addCaseSetDTO == null) {
            log.error("参数为空!");
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }
//        String setId = caseSetService.addCaseSet(addCaseSetDTO);
        String setId = caseSetService.addCaseSetV1(addCaseSetDTO);
        return PtResult.success(setId);
    }

    @PostMapping("/caseSet/updateCaseSet")
    public PtResult<String> updateCaseSet(@RequestBody AddCaseSetDTO addCaseSetDTO) {

        String caseSetId = caseSetService.updateCaseSetV1(addCaseSetDTO);
        return PtResult.success(caseSetId);
    }

    @PostMapping("/caseSet/caseSetList")
    public PageResult<CaseSetWithProjectDTO> getCaseSetList(@RequestBody QueryCaseSetListDTO queryCaseSetListDTO) {
        return caseSetService.queryCaseSetList(queryCaseSetListDTO);
    }

    @PostMapping("/caseSet/executeSet")
    public PtResult<String> executeSet(@RequestBody CaseSetExecuteDTO caseSetExecuteDTO) {
        String caseSetId = caseSetExecuteDTO.getCaseSetId();
        boolean isSuccess;
        if (caseSetId == null) {
            log.error("集合id为空!");
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }
        try {
            caseSetService.doCaseSetCore(caseSetId);
            isSuccess = true;
        } catch (ATPlatformException e) {
            e.printStackTrace();
            isSuccess = false;
            log.error(e.getMessage());
        }
        if (Boolean.TRUE.equals(isSuccess)) {
            return PtResult.success("execute success!");
        } else {
            return PtResult.error("execute failed!!", ExceptionEnum.CALL_FAILED.getErrCode());
        }

    }

    @GetMapping("/caseSet/getCaseSetDetail")
    public PtResult<TCSDetailDTO> getCaseSetDetail(String setId) {
        TCSDetailDTO caseSetDetail = caseSetService.getCaseSetDetailV1(setId);
        return PtResult.success(caseSetDetail);
    }

//    @PostMapping("/caseSet/test")
//    public PtResult<String> test(){
//        caseSetService.parameterExtract("87318735395635200");
//        return PtResult.success("yes");
//    }
}
