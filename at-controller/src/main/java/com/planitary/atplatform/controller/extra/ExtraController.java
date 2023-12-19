package com.planitary.atplatform.controller.extra;

import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PtResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.service.handler.ExcelReaderHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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

    @Resource
    ExcelReaderHandler excelReaderHandler;

    @PostMapping("/exe/excel/getExcelByLocalFile")
    public PtResult<Map<String,List<String>>> getExcel(String path) {
        if (null == path) {
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }
        Map<String,List<String>> valueListMap = new HashMap<>();
        excelReaderHandler.localFileParse(path,valueListMap);
        return PtResult.success(valueListMap);
    }

    @PostMapping("/exe/excel/getExcelByUploadFile")
    public PtResult<Map<String,String>> getExcel(@RequestParam("file")MultipartFile file){
        Map<String,List<String>> valueListMap = new HashMap<>();
        excelReaderHandler.uploadFileParse(file,valueListMap);
        Map<String,String> resMap = new HashMap<>();

        if (valueListMap.size() > 0){
            resMap.put("rows",String.valueOf(valueListMap.size()));
        }
        else {
            log.info("无内容");
            resMap.put("msg","数据为空");
        }
        return PtResult.success(resMap);

    }

}
