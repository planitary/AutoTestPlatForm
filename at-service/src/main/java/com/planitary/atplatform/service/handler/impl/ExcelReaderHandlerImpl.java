package com.planitary.atplatform.service.handler.impl;

import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.service.handler.ExcelReaderHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.FillPatternType;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

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

    // 全局结果记录初值
    private static final Integer BASE_KEY = 1;

    @Override
    public void localFileParse(String localFilePath, Map<String, List<String>> excelValueList) {
        checkFile(localFilePath);
        int baseKey = BASE_KEY;
        try (FileInputStream fileInputStream = new FileInputStream(localFilePath)) {
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            // 获取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            // 从第二行开始遍历
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                List<String> valueList = new ArrayList<>();
                // 遍历每一列
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    this.formatCellValue(cell);
                    // 获取单元格的值
                    valueList.add(cell.toString());
                }
                excelValueList.put(Integer.toString(baseKey), valueList);
                baseKey++;
            }
            log.info("文件'{}'解析成功", localFilePath);
            workbook.close();
        } catch (IOException | EncryptedDocumentException | InvalidFormatException ex) {
            log.error("文件'{}'解析失败!", localFilePath);
            ex.printStackTrace();
        }
    }

    @Override
    public void uploadFileParse(MultipartFile multipartFile,Map<String,List<String>> excelValueListMap) {
        checkFile(multipartFile);
        // 写入临时文件
        try {
            int baseKey = BASE_KEY;
            File tempFile = File.createTempFile("temp" + System.currentTimeMillis(), ".xlsx");
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            outputStream.write(multipartFile.getBytes());
            outputStream.close();
            // 处理excel文件
            FileInputStream inputStream = new FileInputStream(tempFile);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1;i < sheet.getPhysicalNumberOfRows();i++){
                Row row = sheet.getRow(i);
                List<String> unitValueList = new ArrayList<>();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    this.formatCellValue(cell);
                    unitValueList.add(cell.toString());
                }
                excelValueListMap.put(Integer.toString(baseKey),unitValueList);
                baseKey ++;
            }
            log.info("临时文件'{}'解析成功", tempFile.getName());
            workbook.close();
            inputStream.close();
            // 删除临时文件
            if (!tempFile.delete()){
                log.error("临时文件{}删除失败!",tempFile);
                ATPlatformException.exceptionCast(ExceptionEnum.UNKNOWN_ERROR);
            }

        }catch (IOException  e){
            log.error("文件流读取失败!");
            e.printStackTrace();
        }catch (InvalidFormatException e){
            log.error("不支持的文件格式");
            e.printStackTrace();
        }

    }

    @Override
    public Workbook createExcelTemplate() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("sheet1");

        Row headerRow = sheet.createRow(0);
        // 设置单元格样式
        CellStyle unitStyle = workbook.createCellStyle();
        unitStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        unitStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        String[] headers = {"姓名","id","性别","字段4"};
        // 循环往单元格中填入字段
        for (int i = 0;i < headers.length;i++){
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(unitStyle);
        }
        log.info("模板文件生成成功");
        return workbook;
    }

    @Override
    public byte[] workbook2ByteArray(Workbook workbook) {
        if (workbook == null){
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            workbook.write(byteArrayOutputStream);
        }catch (IOException e){
            log.error("流转换失败");
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void checkFile(String localFilePath) {
        File file = new File(localFilePath);
        if (!file.exists()) {
            ATPlatformException.exceptionCast(ExceptionEnum.FILE_NOT_EXIST);
        }
    }

    private void checkFile(MultipartFile file) {
        if (file == null) {
            ATPlatformException.exceptionCast(ExceptionEnum.FILE_NOT_EXIST);
        }
        // 校验文件名
        String fileSuffix = file.getOriginalFilename();
        if (fileSuffix != null) {
            if (!fileSuffix.endsWith("xls") && !fileSuffix.endsWith("xlsx")) {
                log.error("后缀名不是excel格式,原文件后缀名:{}",fileSuffix);
                ATPlatformException.exceptionCast(ExceptionEnum.SUFFIX_NAME_IS_INVALID);
            }
        }
    }

    // 格式化读入数字（String类型读取，以防1被解析成1.0)
    private void formatCellValue(Cell cell) {
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
    }
}
