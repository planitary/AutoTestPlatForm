package com.planitary.atplatform.controller.extra;

import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.customResult.PtResult;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.service.handler.ExcelReaderHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @PostMapping("/exe/excel/getExcel")
    public PtResult<String> getExcel(String path) {
        if (null == path) {
            ATPlatformException.exceptionCast(ExceptionEnum.PARAMETER_ERROR);
        }
        excelReaderHandler.localFileParse(path);
        return PtResult.success("ok");
    }
}
