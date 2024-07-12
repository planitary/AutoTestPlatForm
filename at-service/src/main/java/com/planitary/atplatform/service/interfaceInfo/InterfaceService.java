package com.planitary.atplatform.service.interfaceInfo;

import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.model.dto.*;
import com.planitary.atplatform.model.po.PageParams;
import com.planitary.atplatform.model.po.ATPlatformInterfaceInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

    PageResult<InterfaceWithProjectDTO> queryInterfaceInfoList(QueryInterfaceDTO queryInterfaceDTO);

    /**
     *  获取接口详情
     * @param interfaceId       接口id
     * @return
     */
    ATPlatformInterfaceInfo getInterfaceDetail(BaseInterfaceDTO baseInterfaceDTO);

    /**
     * 通过接口名搜索接口
     * @param baseInterfaceDTO
     * @return
     */

    List<ATPlatformInterfaceInfo> getInterfaceDetailByName(BaseInterfaceDTO baseInterfaceDTO);

    /**
     * 更新接口
     * @param projectId                 项目id
     * @param atPlatformInterfaceInfo   接口实体类
     * @return
     */
    Map<String,String> updateInterface(String projectId,ATPlatformInterfaceInfo atPlatformInterfaceInfo);
    Map<String,String> updateInterfaceV2(ATPlatformInterfaceInfo atPlatformInterfaceInfo);

    /**
     * 解析批量添加接口的Excel
     * @param file          上传的Excel文件
     * @return
     */
    // TODO: 2024/7/12 这里要调整，上传文件由前端来完成，后端只需要接受前端创建的文件流即可
    //  可能与下方的batchAddInterfaceByExcel合并为一个接口
    List<AddInterfaceDTO> parseBatchAddExcelFile(MultipartFile file,String projectId) throws IOException;


    /**
     * 解析excel，并根据excel批量添加接口
     */
    Map<String,Object> batchAddInterfaceByExcel(List<AddInterfaceDTO> addInterfaceDTOS);

    /**
     * 删除接口
     * @param interfaceId           接口id
     * @return
     */
    String deleteInterface(String interfaceId);

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
