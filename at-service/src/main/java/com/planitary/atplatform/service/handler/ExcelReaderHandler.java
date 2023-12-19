package com.planitary.atplatform.service.handler;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.service.handler.impl
 * @name：ExcelReaderHandler
 * @Date：2023/12/18 9:33 下午
 * @Filename：ExcelReaderHandler
 * @description：
 *
 * excel 处理器
 */
public interface ExcelReaderHandler {

    /**
     * 解析本地excel文件
     * @param localFilePath       本地文件路径
     */
    void localFileParse(String localFilePath, Map<String,List<String>> excelValueList);

    /**
     * 解析上传的excel文件
     * @param multipartFile       上传的excel文件
     */
    void uploadFileParse(MultipartFile multipartFile,Map<String,List<String>> excelValueList);
}
