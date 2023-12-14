package com.planitary.atplatform.model.dto;

import com.planitary.atplatform.model.po.ATPlatformCaseSet;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：ExtractParameterDTO
 * @Date：2023/12/14 9:32 下午
 * @Filename：ExtractParameterDTO
 * @description：
 * 接口提取的参数DTO类
 */
public class ExtractParameterDTO extends ATPlatformCaseSet {

    /**
     * 当前事件的业务id
     */
    private String bizId;

}
