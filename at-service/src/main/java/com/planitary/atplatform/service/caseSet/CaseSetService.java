package com.planitary.atplatform.service.caseSet;

import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.model.dto.CaseSetWithProjectDTO;
import com.planitary.atplatform.model.dto.TCSDetailDTO;
import com.planitary.atplatform.model.po.PageParams;
import com.planitary.atplatform.model.dto.AddCaseSetDTO;
import com.planitary.atplatform.model.dto.QueryCaseSetListDTO;
import com.planitary.atplatform.model.po.ATPlatformCaseSet;

import java.util.List;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.service.caseSet
 * @name：CaseSetService
 * @Date：2023/12/14 10:09 下午
 * @Filename：CaseSetService
 * @description：
 */
public interface CaseSetService {

    /**
     * (已弃用，在V1版本中重构)新增一个集合，集合中包含了一个接口id的列表，通过接口id的列表来确定一个唯一的集合
     * 这里的逻辑是新建测试集合的时候，每一条接口会附带一个要提取的参数值（如果有的话）
     *      * 点击新建提交的时候，会按照维护的接口顺序维护参数
     * @param addCaseSetDTO
     * @return
     */
    @Deprecated
    String addCaseSet(AddCaseSetDTO addCaseSetDTO);

    /**
     * 编辑测试用例集合(已弃用)
     * @param addCaseSetDTO         caseSetDTO类
     * @return
     */
    @Deprecated
    String updateCaseSet(AddCaseSetDTO addCaseSetDTO);

    /**
     * 不使用关联表的更新
     * @param addCaseSetDTO
     * @return
     */
    String updateCaseSetV1(AddCaseSetDTO addCaseSetDTO);

    /**
     * 查询测试集合列表
     * @param queryCaseSetListDTO       查询DTO
     * @return
     */
    PageResult<CaseSetWithProjectDTO> queryCaseSetList(QueryCaseSetListDTO queryCaseSetListDTO);

    /**
     * 测试用例核心执行类
     * 执行时按照接口顺序依次进行执行，并按照参数提取列表从接口的返回体中提取结果
     * @param caseSetId            集合id
     */
    void doCaseSetCore(String caseSetId);

    /**
     * 获取集合详情(已弃用)
     * @param setId             测试集合id
     */
    @Deprecated
    TCSDetailDTO getCaseSetDetail(String setId);

    /**
     * 不使用关联关系表查询，分别查询tcs表和interface表，聚合展示结果
     * @param setId
     * @return
     */
    TCSDetailDTO getCaseSetDetailV1(String setId);

    /**
     * 不使用关联关系表的新增
     * @param addCaseSetDTO
     * @return
     */
    String addCaseSetV1(AddCaseSetDTO addCaseSetDTO);

}
