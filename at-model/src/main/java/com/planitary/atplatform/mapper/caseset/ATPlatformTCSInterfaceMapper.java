package com.planitary.atplatform.mapper.caseset;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.planitary.atplatform.model.dto.TCSDetailDTO;
import com.planitary.atplatform.model.po.ATPlatformTCSInterface;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.mapper
 * @name：ATPlatformTCSInterfaceMapper
 * @Date：2024/5/28 9:45 下午
 * @Filename：ATPlatformTCSInterfaceMapper
 * @description：
 */
@Mapper
public interface ATPlatformTCSInterfaceMapper extends BaseMapper<ATPlatformTCSInterface> {
//
//    @Insert("<script>" +
//            "INSERT INTO set_interface (set_id, interface_id) VALUES " +
//            "<foreach collection='list' item='item' separator=','>" +
//            "(#{item.setId}, #{item.interfaceId})" +
//            "</foreach>" +
//            "</script>")
    Integer insertBatchTCSInterface(@Param("interfaceList") List<ATPlatformTCSInterface> list);

    List<TCSDetailDTO> getTCSDetail(@Param("setId") String setId);
}
