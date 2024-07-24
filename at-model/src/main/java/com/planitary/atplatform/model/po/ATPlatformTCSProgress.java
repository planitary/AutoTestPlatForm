package com.planitary.atplatform.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：InterfaceInfoSIP
 * @Date：2024/7/24 11:27 上午
 * @Filename：InterfaceInfoSIP
 * @description：       测试用例步骤详情
 */
@Data
public class ATPlatformTCSProgress {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 用例id
     */
    private String casesetId;

    /**
     * 记录id
     */
    private String progressId;

    /**
     * 当前记录的序位（step序号)
     */

    private String progressSeq;

    /**
     * 步骤名
     */
    private String progressName;

    /**
     * 步骤执行方式
     */
    private String operationType;
    /**
     * step1的json(基本信息）
     */
    private String step1Content;

    /**
     * step2的json(详细信息)
     */
    private String step2Content;

    /**
     * step3的json(断言)
     */
    private String step3Content;

    /**
     * step4的json(额外配置)
     */
    private String step4Content;

}
