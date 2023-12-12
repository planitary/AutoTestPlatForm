package com.planitary.atplatform.service;

import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.handler.PageParams;
import com.planitary.atplatform.model.dto.AddInterfaceDTO;
import com.planitary.atplatform.model.dto.QueryInterfaceInfoDTO;
import com.planitary.atplatform.model.po.ATPlatformInterfaceInfo;

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

    /**
     * 接口查询列表
     * @param pageParams                分页参数
     * @param queryInterfaceInfoDTO     接口查询参数,注意必须要有projectId
     * @return
     */
    PageResult<ATPlatformInterfaceInfo> queryInterfaceInfoList(PageParams pageParams, QueryInterfaceInfoDTO queryInterfaceInfoDTO);


    Map<String,String> updateInterface(String projectId,ATPlatformInterfaceInfo atPlatformInterfaceInfo);


}
