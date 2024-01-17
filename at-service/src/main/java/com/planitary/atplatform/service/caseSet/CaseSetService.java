package com.planitary.atplatform.service.caseSet;

import com.planitary.atplatform.base.customResult.PageResult;
import com.planitary.atplatform.base.handler.PageParams;
import com.planitary.atplatform.model.dto.AddCaseSetDTO;
import com.planitary.atplatform.model.dto.QueryCaseSetListDTO;
import com.planitary.atplatform.model.po.ATPlatformCaseSet;

import javax.print.attribute.standard.PageRanges;
import java.util.Map;
import java.util.Set;

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
     * 新增一个集合，集合中包含了一个接口id的列表，通过接口id的列表来确定一个唯一的集合
     * @param addCaseSetDTO
     * @return
     */
    String addCaseSet(AddCaseSetDTO addCaseSetDTO);

    /**
     * 编辑测试用例集合
     * @param addCaseSetDTO         caseSetDTO类
     * @return
     */
    String updateCaseSet(AddCaseSetDTO addCaseSetDTO);

    /**
     * 查询测试集合列表
     * @param pageParams
     * @param queryCaseSetListDTO
     * @return
     */
    PageResult<ATPlatformCaseSet> queryCaseSetList(PageParams pageParams, QueryCaseSetListDTO queryCaseSetListDTO);

    /**
     * json提取接口调用的参数
     * 这里的逻辑是新建测试集合的时候，每一条接口会附带一个要提取的参数值（如果有的话），点击新建提交的时候，会按照维护的接口顺序维护参数
     * @param parameters            待提取的参数集合
     */
    void parameterExtract(Set<String> parameters);
}
