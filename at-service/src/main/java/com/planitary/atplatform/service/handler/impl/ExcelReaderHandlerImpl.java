package com.planitary.atplatform.service.handler.impl;

import com.alibaba.fastjson.JSON;
import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.planitary.atplatform.base.commonEnum.ExceptionEnum;
import com.planitary.atplatform.base.exception.ATPlatformException;
import com.planitary.atplatform.model.dto.ExcelParseDTO;
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
    public List<ExcelParseDTO> localFileParse(String localFilePath) {
        checkFile(localFilePath);
        int baseKey = BASE_KEY;
        try (FileInputStream fileInputStream = new FileInputStream(localFilePath)) {
            List<ExcelParseDTO> excelParseDTOList = parseExcel(fileInputStream);
            log.info("文件'{}'解析成功", localFilePath);
            return excelParseDTOList;
        } catch (IOException | EncryptedDocumentException | InvalidFormatException ex) {
            log.error("文件'{}'解析失败!", localFilePath);
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ExcelParseDTO> uploadFileParse(MultipartFile multipartFile) {
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
            List<ExcelParseDTO> excelParseDTOList = parseExcel(inputStream);
            log.info("临时文件'{}'解析成功", tempFile.getName());
            inputStream.close();
            // 删除临时文件
            if (!tempFile.delete()) {
                log.error("临时文件{}删除失败!", tempFile);
                ATPlatformException.exceptionCast(ExceptionEnum.UNKNOWN_ERROR);
            }
            return excelParseDTOList;
        } catch (IOException e) {
            log.error("文件流读取失败!");
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            log.error("不支持的文件格式");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * excel解析类
     * @param inputStream                   excel的文件输入流
     * @return                              excel文件解析数据
     * @throws IOException
     * @throws InvalidFormatException
     */
    public List<ExcelParseDTO> parseExcel(FileInputStream inputStream) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<ExcelParseDTO> excelParseDTOList = new ArrayList<>();

        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            ExcelParseDTO excelParseDTO = new ExcelParseDTO();

            Cell cell0 = row.getCell(0);
            if (cell0 != null && cell0.getCellType() == Cell.CELL_TYPE_STRING) {
                excelParseDTO.setInterfaceUrl(cell0.getStringCellValue());
            }
            else {
                ATPlatformException.exceptionCast("接口url不能为空");
            }

            Cell cell1 = row.getCell(1);
            if (cell1 != null && cell1.getCellType() == Cell.CELL_TYPE_STRING) {
                excelParseDTO.setRequestBody(cell1.getStringCellValue());
            }
            else {
                ATPlatformException.exceptionCast("接口入参不能为空");
            }

            Cell cell2 = row.getCell(2);
            if (cell2 != null && cell2.getCellType() == Cell.CELL_TYPE_STRING) {
                excelParseDTO.setOwnerName(cell2.getStringCellValue());
            }
            excelParseDTOList.add(excelParseDTO);
        }
        workbook.close();
        return excelParseDTOList;
    }

    @Override
    public Workbook createExcelTemplate() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("sheet1");

        Row headerRow = sheet.createRow(0);
        Row exampleRow = sheet.createRow(1);
        // 设置单元格样式
        CellStyle unitStyle = workbook.createCellStyle();
        unitStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        unitStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        String[] headers = {"接口url(*)", "入参requestBody(*)", "维护人"};
        Map<String,String> exampleMap = new HashMap<>();
        // 设置样例
        exampleMap.put("id","100");
        exampleMap.put("param1","abc");
        exampleMap.put("param2","[1,5,7]");
        String exampleJSON = JSON.toJSONString(exampleMap);
        String[] exampleValue = {"/test/getList",exampleJSON,"planitary"};
        // 循环往单元格中填入字段
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(unitStyle);
        }

        for (int i = 0;i < exampleValue.length;i++){
            Cell cell = exampleRow.createCell(i);
            cell.setCellValue(exampleValue[i]);
        }

        log.info("模板文件生成成功");
        return workbook;
    }

    @Override
    public byte[] workbook2ByteArray(Workbook workbook) {
        if (workbook == null) {
            ATPlatformException.exceptionCast(ExceptionEnum.OBJECT_NULL);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            workbook.write(byteArrayOutputStream);
        } catch (IOException e) {
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
                log.error("后缀名不是excel格式,原文件后缀名:{}", fileSuffix);
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
