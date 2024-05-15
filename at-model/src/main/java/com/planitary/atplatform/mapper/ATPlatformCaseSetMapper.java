package com.planitary.atplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.planitary.atplatform.model.dto.CaseSetWithProjectDTO;
import com.planitary.atplatform.model.dto.QueryCaseSetListDTO;
import com.planitary.atplatform.model.po.ATPlatformCaseSet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.mapper
 * @name：ATPlatformCaseSetMapper
 * @Date：2023/12/14 9:57 下午
 * @Filename：ATPlatformCaseSetMapper
 * @description：
 */
@Mapper
public interface ATPlatformCaseSetMapper extends BaseMapper<ATPlatformCaseSet> {
    Page<CaseSetWithProjectDTO> getCaseSetList(Page<CaseSetWithProjectDTO> page, @Param("queryParam")QueryCaseSetListDTO queryCaseSetListDTO);

}
