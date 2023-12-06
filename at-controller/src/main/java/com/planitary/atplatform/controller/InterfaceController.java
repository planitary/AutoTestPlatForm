package com.planitary.atplatform.controller;

import com.planitary.atplatform.base.customResult.PtResult;
import com.planitary.atplatform.model.dto.AddInterfaceDTO;
import com.planitary.atplatform.model.dto.AddProjectDTO;
import com.planitary.atplatform.model.dto.ExecuteDTO;
import com.planitary.atplatform.model.dto.ExecuteResponseDTO;
import com.planitary.atplatform.service.ExecuteHandler;
import com.planitary.atplatform.service.InterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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
}
