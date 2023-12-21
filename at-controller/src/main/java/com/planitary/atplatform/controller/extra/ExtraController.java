package com.planitary.atplatform.controller.extra;

import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PtResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.model.dto.ExcelParseDTO;
import com.planitary.atplatform.service.handler.ExcelReaderHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.controller.extra
 * @name：ExtraController
 * @Date：2023/12/18 10:27 下午
 * @Filename：ExtraController
 * @description：
 */
@RestController
@Slf4j
public class ExtraController {

    public static final String EXCEL_MIME = "application/octet-stream";
    public static final String TEST_CASE_TEMPLATE_FILENAME = "Testcase_";

    @Resource
    ExcelReaderHandler excelReaderHandler;

    @PostMapping("/exe/excel/getExcelByLocalFile")
    public PtResult<List<ExcelParseDTO>> getExcel(String path) {
        if (null == path) {
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }
        List<ExcelParseDTO> excelParseDTOList = excelReaderHandler.localFileParse(path);
        if (excelParseDTOList == null){
            log.error("文件内容为空或格式不正确");
            ATPlatformException.exceptionCast(ExceptionEnum.PARSE_FAILED);
        }
        return PtResult.success(excelParseDTOList);
    }

    @PostMapping("/exe/excel/getExcelByUploadFile")
    public PtResult<List<ExcelParseDTO>> getExcel(@RequestParam("file")MultipartFile file){
        List<ExcelParseDTO> excelParseDTOList = excelReaderHandler.uploadFileParse(file);
        if (excelParseDTOList == null){
            log.error("文件内容为空或格式不正确");
            ATPlatformException.exceptionCast(ExceptionEnum.PARSE_FAILED);
        }
        return PtResult.success(excelParseDTOList);
    }

    @RequestMapping("/exe/excel/getTestCaseTemplate")
    public ResponseEntity<InputStreamResource> downloadTestCaseTemplate(){
        Workbook workbook = excelReaderHandler.createExcelTemplate();
        String fileName = TEST_CASE_TEMPLATE_FILENAME + System.currentTimeMillis() + ".xlsx";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition","attachment; filename=" + fileName);
        log.info("模板文件名:{}",fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(EXCEL_MIME))
                .body(new InputStreamResource(new ByteArrayInputStream(excelReaderHandler.workbook2ByteArray(workbook))));
    }
}
