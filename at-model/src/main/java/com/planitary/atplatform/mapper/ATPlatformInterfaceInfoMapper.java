package com.planitary.atplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.planitary.atplatform.model.dto.InterfaceWithProjectDTO;
import com.planitary.atplatform.model.dto.ParamDTO;
import com.planitary.atplatform.model.dto.QueryInterfaceDTO;
import com.planitary.atplatform.model.po.ATPlatformInterfaceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.mapper
 * @name：ATTestInterfaceInfoMapper
 * @Date：2023/12/1 10:38 下午
 * @Filename：ATTestInterfaceInfoMapper
 * @description：
 */
@Mapper
public interface ATPlatformInterfaceInfoMapper extends BaseMapper<ATPlatformInterfaceInfo> {

    Page<InterfaceWithProjectDTO> getInterfaceWithProject(Page<InterfaceWithProjectDTO> page, @Param("queryParam") QueryInterfaceDTO queryInterfaceDTO);


}
