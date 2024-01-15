package com.planitary.atplatform.controller.interfaceInfo;

import com.planitary.atplatform.base.commonEnum.BizCodeEnum;
import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.customResult.PtResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.base.handler.PageParams;
import com.planitary.atplatform.model.dto.*;
import com.planitary.atplatform.model.po.ATPlatformInterfaceInfo;
import com.planitary.atplatform.service.handler.ExecuteHandler;
import com.planitary.atplatform.service.interfaceInfo.InterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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


    @GetMapping("/interface/interfaceList")
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
     * 这里整个的逻辑是先新建接口，创建requestBody--->填写excel文件(接口执行的集合以及参数集合）
     * ---> 前端选择要执行的接口以及对应的参数
     * @param chosenParamDTO                前端封装的接口选择类（包含接口url和参数集合）
     * @return
     */
    @PostMapping("/interface/parameterizedExecution")
    public PtResult<String> fillRequestBody(@RequestBody ChosenParamDTO chosenParamDTO){
        interfaceService.coreFillParameter(chosenParamDTO);
        interfaceService.coreExecutor(BizCodeEnum.EXTERNAL_INTERFACE_CALL);
        return PtResult.success("Success");
    }

    @PostMapping("/external/testInterface")
    public PtResult<String> testInterface(@RequestBody Map<String,Object> param){
        String res = "test:" + param.get("ocCode") + " name:" + param.get("ocName");
        return PtResult.success(res);
    }
}
