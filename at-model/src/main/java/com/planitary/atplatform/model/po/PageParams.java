package com.planitary.atplatform.model.po;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.base.handle
 * @name：PageParams
 * @Date：2023/11/29 9:47 下午
 * @Filename：PageParams
 * @description：
 */

import lombok.Data;
import lombok.ToString;

/**
 * 通用分页查询参数，后续查询相关的DTO类直接继承，将分页参数耦合进查询DTO中
 */
@Data
@ToString
public class PageParams {
    // 默认分页初值值和页面大小
    public static final long DEFAULT_CURRENT_PAGE = 1L;
    public static final long DEFAULT_PAGE_SIZE = 10L;

    private long pageNo = DEFAULT_CURRENT_PAGE;
    private long pageSize = DEFAULT_PAGE_SIZE;

    public PageParams(){}

    public PageParams(long pageNo,long pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}
