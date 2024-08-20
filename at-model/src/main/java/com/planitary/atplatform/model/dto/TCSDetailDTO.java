package com.planitary.atplatform.model.dto;

import com.planitary.atplatform.model.po.ATPlatformCaseSet;
import com.planitary.atplatform.model.po.ATPlatformInterfaceInfo;
import com.planitary.atplatform.model.po.ATPlatformTCSStep;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.dto
 * @name：TCSDetail
 * @Date：2024/5/28 10:36 下午
 * @Filename：TCSDetail
 * @description：  一个典型的dto如下
 * {
 * "setId":"123124124",
 * "projectId":"238492342",
 * "setName":"test_poi",
 * "setWeight":8,
 * "parameterList":"",
 * "remark":"test",
 * "interfaceInfos":[
 * {
 * "interfaceId":"324234234",
 * "interfaceName":"测试接口1",
 * "interfaceUrl":"/monitor/ops2"
 * },{
 * "interfaceId":"324234237",
 * "interfaceName":"测试接口2",
 * "interfaceUrl":"/sm/mall/test2"
 * }]
 *
 *
 * }
 */

/**
 * 测试集合详情
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TCSDetailDTO extends ATPlatformCaseSet {
    private String interfaceId;
    private String interfaceName;
    private String interfaceUrl;
    private String interfaceRemark;
    // 接口请求方式
    private String methodType;
    private String requestBody;
    private String projectName;
    // 步骤详情
    private List<ATPlatformTCSStep> stepsData;
}
