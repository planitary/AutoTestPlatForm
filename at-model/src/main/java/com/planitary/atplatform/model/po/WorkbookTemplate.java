package com.planitary.atplatform.model.po;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.model.po
 * @name：WorkbookTemplate
 * @Date：2024/6/25 8:55 下午
 * @Filename：WorkbookTemplate
 * @description：        表格模板对象
 */
@Data
public class WorkbookTemplate {
    // 表头项
    private List<String> headers;
    // 单元格的值
    private List<Map<String,Object>> params;
    // 表格用途
    private String name;
}
