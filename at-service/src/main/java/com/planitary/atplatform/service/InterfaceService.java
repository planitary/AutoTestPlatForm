package com.planitary.atplatform.service;

import com.planitary.atplatform.model.dto.AddInterfaceDTO;

import java.util.Map;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.service
 * @name：InterfaceService
 * @Date：2023/12/5 9:47 下午
 * @Filename：InterfaceService
 * @description：
 */

public interface InterfaceService {

    /**
     * 新增接口，先有的project，再有的interface，所以新增接口必须先拿到对应的projectId
     * @param addInterfaceDTO       新增接口参数
     */

    Map<String,String> insertInterface(AddInterfaceDTO addInterfaceDTO);
}
