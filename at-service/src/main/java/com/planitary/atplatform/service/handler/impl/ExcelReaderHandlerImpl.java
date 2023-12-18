package com.planitary.atplatform.service.handler.impl;

import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.service.handler.ExcelReaderHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author：planitary
 * @Package：com.planitary.atplatform.service.handler.impl
 * @name：ExcelReaderHandlerServiceImpl
 * @Date：2023/12/18 9:49 下午
 * @Filename：ExcelReaderHandlerServiceImpl
 * @description：
 */
@Slf4j
@Service
public class ExcelReaderHandlerImpl implements ExcelReaderHandler {


    @Override
    public void localFileParse(String localFilePath) {
        List<String> cellList = new ArrayList<>();
        checkFile(localFilePath);
        try(FileInputStream fileInputStream = new FileInputStream(localFilePath)){
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            // 获取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            // 从第二行开始遍历
            for (Row row : sheet) {
                // 遍历每一列
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    this.formatCellValue(cell);
                    // 获取单元格的值
                    System.out.print(cell.toString() + "\t");
                }
                System.out.println();
            }
            log.info("文件'{}'解析成功", localFilePath);
        }catch (IOException | EncryptedDocumentException | InvalidFormatException ex){
            log.error("文件'{}'解析失败!",localFilePath);
            ex.printStackTrace();
        }
    }

    @Override
    public void uploadFileParse(MultipartFile multipartFile) {

    }

    private void checkFile(String localFilePath){
        File file = new File(localFilePath);
        if (!file.exists()){
            ATPlatformException.exceptionCast(ExceptionEnum.FILE_NOT_EXIST);
        }
    }
    // 格式化读入数字（String类型读取，以防1被解析成1.0)
    private  void formatCellValue(Cell cell){
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
    }
}
