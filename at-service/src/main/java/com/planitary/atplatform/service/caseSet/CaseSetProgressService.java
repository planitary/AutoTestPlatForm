package com.planitary.atplatform.service.caseSet;

import com.planitary.atplatform.model.dto.AddTCSProgressDTO;

import java.util.List;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.service.caseSet.impl
 * @name：CaseSetServiceImpl
 * @Date：2024/08/23 16:20 下午
 * @Filename：CaseSetServiceImpl
 * @description：
 */
public interface CaseSetProgressService {

    /**
     * 添加
     * @param addTCSProgressDTO
     * @return
     */
    public String addTCSProgress(List<AddTCSProgressDTO> addTCSProgressDTOs);
}
