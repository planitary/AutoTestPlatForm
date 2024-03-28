package com.planitary.atplatform.controller.interfaceInfo;

import com.planitary.atplatform.base.commonEnum.BizCodeEnum;
import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.customResult.PtResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.model.po.PageParams;
import com.planitary.atplatform.model.dto.*;
import com.planitary.atplatform.model.po.ATPlatformInterfaceInfo;
import com.planitary.atplatform.service.handler.ExecuteHandler;
import com.planitary.atplatform.service.interfaceInfo.InterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.controller
 * @name：InterfaceController
 * @Date：2023/12/5 10:01 下午
 * @Filename：InterfaceController
 * @description：
 */
@RestController
@Slf4j
@CrossOrigin
public class InterfaceController {

    @Resource
    InterfaceService interfaceService;

    @Resource
    ExecuteHandler executeHandler;

    @PostMapping("/interface/addInterface")
    public PtResult<?> addInterface(@RequestBody AddInterfaceDTO addInterfaceDTO){
        Map<String, String> map = interfaceService.insertInterface(addInterfaceDTO);
        return PtResult.success(map);
    }

    @PostMapping("/mock/addInterface")
    public PtResult<?> addInterface(){
        AddInterfaceDTO addInterfaceDTO = new AddInterfaceDTO();
        addInterfaceDTO.setInterfaceUrl("/geofence/getGeofence");
        addInterfaceDTO.setInterfaceName("获取电子地图围栏");
        addInterfaceDTO.setProjectId("41859521183");
        Map<String, String> map = interfaceService.insertInterface(addInterfaceDTO);
        return PtResult.success(map);
    }

    @PostMapping("/interface/executeInterface")
    public PtResult<?> execute(@RequestBody ExecuteDTO executeDTO){
        ExecuteResponseDTO executeResponseDTO = executeHandler.doInterfaceExecutor(executeDTO);
        return PtResult.success(executeResponseDTO);
    }


    @RequestMapping("/interface/interfaceList")
    public PageResult<ATPlatformInterfaceInfo> getInterfaceList(PageParams pageParams, @RequestBody QueryInterfaceInfoDTO queryInterfaceInfoDTO){
        return interfaceService.queryInterfaceInfoList(pageParams,queryInterfaceInfoDTO);
    }

    @PostMapping("/interface/updateInterface")
    public PtResult<Map<String,String>> updateInterface(String projectId, @RequestBody ATPlatformInterfaceInfo atPlatformInterfaceInfo){
        if (projectId == null){
            log.error("项目id为空");
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }
        Map<String, String> resMap = interfaceService.updateInterface(projectId, atPlatformInterfaceInfo);
        return PtResult.success(resMap);
    }

    /**
     * 这里整个的逻辑是先新建接口，同时填写接口的requestBody--->填写excel文件(接口执行的集合以及参数集合）
     * ---> 前端选择要执行的接口以及对应的参数，并选择excel文件的处理目的（1、更新接口，2、批量发起调用）
     * @param chosenParamDTO                前端封装的接口选择类（包含接口url和参数集合）
     * @return
     */
    @PostMapping("/interface/parameterizedExecution")
    public PtResult<String> fillRequestBody(@RequestBody ChosenParamDTO chosenParamDTO){
        String callMethod = chosenParamDTO.getCallMethod();
        // 业务分类,默认发起外部接口调用
        String callMethodCode = BizCodeEnum.EXTERNAL_INTERFACE_CALL.getBizCode();

        if (Objects.equals(callMethod,"EXTERNAL_INTERFACE_CALL")){
            callMethodCode = BizCodeEnum.EXTERNAL_INTERFACE_CALL.getBizCode();
        }
        if (Objects.equals(callMethod,"UPDATE_CURRENT_INTERFACE_INFO")){
            callMethodCode = BizCodeEnum.UPDATE_CURRENT_INTERFACE_INFO.getBizCode();
        }
        interfaceService.coreFillParameter(chosenParamDTO);
        log.info("传递的方法为:{}",callMethodCode);
        interfaceService.coreExecutor(callMethodCode);
        return PtResult.success("Success");
    }

    @PostMapping("/external/testInterface")
    public PtResult<Map<String,Object>> testInterface(@RequestBody Map<String,Object> param){
        Map<String,Object> res = new HashMap<>();
        res.put("id",param.get("id"));
        res.put("name",param.get("name"));
        res.put("tag",param.get("tag"));
        return PtResult.success(res);
    }

    @PostMapping("/external/testInterface2")
    public PtResult<String> testInterface2(@RequestBody Map<String,Object> param){
        String res ="hello" +" " + param.get("id") +" "  + param.get("name") + " " + param.get("tag");
        return PtResult.success(res);
    }
}
