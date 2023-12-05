package com.planitary.atplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.base.utils.GeneralIdGenerator;
import com.planitary.atplatform.mapper.ATTestInterfaceInfoMapper;
import com.planitary.atplatform.mapper.ATTestProjectMapper;
import com.planitary.atplatform.model.dto.AddInterfaceDTO;
import com.planitary.atplatform.model.po.ATTestInterfaceInfo;
import com.planitary.atplatform.model.po.ATTestProject;
import com.planitary.atplatform.service.InterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.service.impl
 * @name：InterfaceServiceImpl
 * @Date：2023/12/5 9:49 下午
 * @Filename：InterfaceServiceImpl
 * @description：
 */
@Service
@Slf4j
public class InterfaceServiceImpl implements InterfaceService {
    @Resource
    private ATTestInterfaceInfoMapper atTestInterfaceInfoMapper;

    @Resource
    private ATTestProjectMapper atTestProjectMapper;


    @Override
    @Transactional
    public Map<String,String> insertInterface(AddInterfaceDTO addInterfaceDTO) {
        // 校验项目的合法性
        LambdaQueryWrapper<ATTestProject> atTestProjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        atTestProjectLambdaQueryWrapper.eq(ATTestProject::getProjectId,addInterfaceDTO.getProjectId());
        ATTestProject atTestProject = atTestProjectMapper.selectOne(atTestProjectLambdaQueryWrapper);
        if (atTestProject == null){
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        ATTestInterfaceInfo interfaceInfo = new ATTestInterfaceInfo();
        String interfaceId = GeneralIdGenerator.generateId() + GeneralIdGenerator.generateId().substring(0,6);
        BeanUtils.copyProperties(addInterfaceDTO,interfaceInfo);
        interfaceInfo.setInterfaceId(interfaceId);
        interfaceInfo.setCreateUser("zane");
        interfaceInfo.setProjectId(addInterfaceDTO.getProjectId());
        interfaceInfo.setVersion(1);
        int insert = atTestInterfaceInfoMapper.insert(interfaceInfo);
        if (insert <= 0){
            log.error("执行失败:{}",ExceptionEnum.INSERT_FAILED.getErrMessage());
            ATPlatformException.exceptionCast(ExceptionEnum.INSERT_FAILED);
        }
        log.debug("插入成功");
        Map<String,String> res = new HashMap<>();
        res.put("interfaceId",interfaceId);
        return res;
    }
}
