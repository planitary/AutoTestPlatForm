package com.planitary.atplatform.service.handler;

import com.planitary.atplatform.model.dto.ExcelParseDTO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
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
    List<ExcelParseDTO> localFileParse(String localFilePath);

    /**
     * 解析上传的excel文件
     * @param multipartFile       上传的excel文件
     */
    List<ExcelParseDTO> uploadFileParse(MultipartFile multipartFile);

    /**
     * 后端配置excel模板
     * @return       返回一个创建好的excel，以WorkBook的形式
     */
    Workbook createExcelTemplate();

    /**
     * workbook转为字节流供下载
     * @param workbook          模板文件
     * @return
     */
    byte[] workbook2ByteArray(Workbook workbook);

    List<ExcelParseDTO> parseExcel(FileInputStream inputStream) throws IOException, InvalidFormatException;
}
