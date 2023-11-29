package com.planitary.atplatform.base.customResult;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.base.customResult
 * @name：PageResult
 * @Date：2023/11/29 9:31 下午
 * @Filename：PageResult
 * @description：
 */

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 自定义分页
 */
@Data
@NoArgsConstructor
@ToString
public class PageResult<T> implements Serializable {
    // 数据列表
    private List<T> items;

    /**
     * 结果编码
     */
    private String code;

    // 总结果数
    private long totalCounts;

    // 当前页码
    private long page;

    // 其他信息
    private Map<String,String> data;

    // 页大小
    private long pageSize;

    public PageResult(List<T> items,long totalCounts,long page,long pageSize,String code){
        this.items = items;
        this.totalCounts = totalCounts;
        this.page = page;
        this.pageSize = pageSize;
        this.code = code;
    }

    public PageResult(List<T> items,long totalCounts,long page,long pageSize,Map<String,String> data,String code){
        this.items = items;
        this.totalCounts = totalCounts;
        this.page = page;
        this.pageSize = pageSize;
        this.data = data;
        this.code = code;
    }

}
