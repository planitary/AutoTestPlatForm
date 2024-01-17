package com.planitary.atplatform.service.interfaceInfo;

import com.planitary.atplatform.base.commonEnum.BizCodeEnum;
import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.handler.PageParams;
import com.planitary.atplatform.model.dto.AddInterfaceDTO;
import com.planitary.atplatform.model.dto.ChosenParamDTO;
import com.planitary.atplatform.model.dto.InterfaceParamDTO;
import com.planitary.atplatform.model.dto.QueryInterfaceInfoDTO;
import com.planitary.atplatform.model.po.ATPlatformInterfaceInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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

    /**
     * 更新接口
     * @param projectId                 项目id
     * @param atPlatformInterfaceInfo   接口实体类
     * @return
     */
    Map<String,String> updateInterface(String projectId,ATPlatformInterfaceInfo atPlatformInterfaceInfo);

    /**
     * 批量填充接口入参（异步封装请求)
     * @param chosenParamMap       选中的参数集合
     * @return
     */
    CompletableFuture<String> coreFillParameter(ChosenParamDTO chosenParamMap);

    /**
     * 填充后的接口执行发起请求
     */
    CompletableFuture<Void> coreExecutor(String callableMethodCode);



}
