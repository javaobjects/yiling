package com.yiling.export.export.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.oss.enums.FileTypeEnum;

import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.poi.excel.ExcelUtil;


/**
 * 导出工具类
 *
 * @author jian.mei
 * @date 2020-05-08
 */
public class ExportExcelUtil {

    private final static int LIMIT_SIZE = 100000;

    private final static int SHEET_SIZE = 50000;

//    public static byte[] listToExcel(List<ExportDataDTO> exportDataDTOList){
//
//
//        Workbook workbook=ExcelExportUtil.exportExcel(List<Map<String, Object>> list, ExcelType type);
//    }
    /**
     * 基于excel模版导出数据
     */
    public static byte[] listToExcelByTemplete(List<ExportDataDTO> exportDataDTOList, ExcelType excelType,String templetePath) throws Exception{
        if(StringUtils.isEmpty(templetePath)){
            throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "参数异常");
        }
        Resource resource = new ClassPathResource(templetePath);
        InputStream inputStream = resource.getInputStream();
        //FileInputStream  fis=new FileInputStream(resource.getFile());
        // 下载网络文件，通过oss的url下载
        /*String url = fileService.getUrl(record.getRequestUrl(), FileTypeEnum.EXCEL_IMPORT_RESULT);
        byte[] bytes = HttpUtil.downloadBytes(url);
        InputStream inputStream = new ByteArrayInputStream(bytes);*/
        // 声明一个工作薄
        Workbook workbook;
        boolean SXSSFFlag=false;
        for(ExportDataDTO exportDataDTO:exportDataDTOList){
            if(CollectionUtil.isNotEmpty(exportDataDTO.getData())
                    && exportDataDTO.getData().size()>10000){
                SXSSFFlag=true;
            }
        }
        if(SXSSFFlag){
            workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream));
        }else {
            workbook = new XSSFWorkbook(inputStream);
        }
        for (ExportDataDTO exportDataDTO : exportDataDTOList) {
            if (StringUtils.isEmpty(exportDataDTO.getData())) {
                throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "参数异常");
            }
            try {
                // 导出每页限制10W
                if (exportDataDTO.getData().size() > LIMIT_SIZE) {
                    exportDataDTO.setData(exportDataDTO.getData().subList(0, LIMIT_SIZE));
                }
                int sheetCount = getSheetCount(exportDataDTO.getData().size(), SHEET_SIZE);
                int firstIndex = 0;
                int lastIndex = 0;
                int sheetIndex=0;
                String sheetName = exportDataDTO.getSheetName();
                if (sheetCount == 0) {
                    fillSheetTemplete(workbook,exportDataDTO.getData(),exportDataDTO.getTempleteParamList(),sheetIndex,exportDataDTO.getFristRow(),"yyyy-MM-dd HH:mm:ss", exportDataDTO.getEnField());
                } else {
                    for (int i = 0; i < sheetCount; i++) {
                        fillSheetTemplete(workbook,exportDataDTO.getData(),exportDataDTO.getTempleteParamList(),sheetIndex,exportDataDTO.getFristRow(),"yyyy-MM-dd HH:mm:ss", exportDataDTO.getEnField());
                        sheetIndex++;
                    }
                }
            } catch (Exception e) {
                throw e;
            }
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        return os.toByteArray();
    }
    /**
     * 向工作表中填充数据
     *
     * @param workbook   工作表
     * @param list       数据源
     * @param templeteParamList 数据参数顺序列表
     * @param firstIndex Sheet索引
     * @param firstRow sheet行数据索引
     * @param format     日期转化为字符串的格式
     * @param enField    需要特殊转换列的英文名和对应的转换规则，不需要设为null
     * @throws Exception
     */
    private static void fillSheetTemplete(Workbook workbook, List<Map<String, Object>> list, LinkedList<String> templeteParamList,int firstIndex,int firstRow , String format, Map<String, Map<Object, String>> enField)throws Exception {
        //获取模版中sheet页
        Sheet sheet = workbook.getSheetAt(firstIndex);
        // 定义存放英文字段名和中文字段名的数组
        String[] enFields = new String[templeteParamList.size()];
        //获取数据行的公共样式
        //Row rowCell = sheet.getRow(firstRow-1);
       // CellStyle styles[] = new CellStyle[rowCell.getLastCellNum()];
        Cell cell = null;
        CellStyle styleData = workbook.createCellStyle();
        styleData.setFillForegroundColor(IndexedColors.WHITE.index);
        styleData.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleData.setBorderBottom(BorderStyle.THIN);
        styleData.setBorderLeft(BorderStyle.THIN);
        styleData.setBorderRight(BorderStyle.THIN);
        styleData.setBorderTop(BorderStyle.THIN);
        styleData.setAlignment(HorizontalAlignment.CENTER);
        styleData.setVerticalAlignment(VerticalAlignment.CENTER);
        ////构造单元格 最后一行
        //获取数据游标从0开始;
        for (int i = 0; i <list.size() ; i++) {
            //创建每一行，同excel模版中数据行开始 传进来的值时减1需要
            Row rowData = sheet.createRow(i+firstRow);
            Map<String, Object> item = list.get(i);
            //行单元格游标循环
            for (int j = 0; j < templeteParamList.size(); j++) {
                cell=rowData.createCell(j, CellType.STRING);
                Object objValue = getFieldValueByName(templeteParamList.get(j), item, format, enField);
                if (objValue instanceof String) {
                    DataFormat formatDate = workbook.createDataFormat();
                    styleData.setDataFormat(formatDate.getFormat("@"));
                }
                cell.setCellStyle(styleData);
                String fieldValue = (objValue == null ? "" : objValue.toString());
                RichTextString text = null;
                text=new XSSFRichTextString(fieldValue);
                cell.setCellValue(text);
            }
        }
    }

    /**
     * 导出Excel表
     *
     * @param exportDataDTOList 数据源
     * @return byte[]
     * @throws Exception
     */
    public static byte[] listToExcel(List<ExportDataDTO> exportDataDTOList, ExcelType excelType) throws Exception {
        if (StringUtils.isEmpty(exportDataDTOList)|| null == excelType) {
            throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "参数异常");
        }
        // 声明一个工作薄
        Workbook workbook;
        if (ExcelType.HSSF.equals(excelType)) {
            workbook = new HSSFWorkbook();
        } else {
            boolean SXSSFFlag=false;
            for(ExportDataDTO exportDataDTO:exportDataDTOList){
                if(CollectionUtil.isNotEmpty(exportDataDTO.getData())
                        && exportDataDTO.getData().size()>10000){
                    SXSSFFlag=true;
                }
            }
            if(SXSSFFlag){
                workbook = new SXSSFWorkbook();
            }else {
                workbook = new XSSFWorkbook();
            }
        }
        // 生成标题行样式
        CellStyle styleTitle = workbook.createCellStyle();
        // 设置这些样式
        styleTitle.setFillForegroundColor(IndexedColors.WHITE.index);
        styleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTitle.setBorderBottom(BorderStyle.THIN);
        styleTitle.setBorderLeft(BorderStyle.THIN);
        styleTitle.setBorderRight(BorderStyle.THIN);
        styleTitle.setBorderTop(BorderStyle.THIN);
        styleTitle.setAlignment(HorizontalAlignment.CENTER);
        // 生成一个字体
        Font fontTitle = workbook.createFont();
        fontTitle.setColor(IndexedColors.BLACK.index);
        fontTitle.setFontHeightInPoints((short) 14);
        fontTitle.setBold(true);
        // 把字体应用到当前的样式
        styleTitle.setFont(fontTitle);

        // 生成数据行样式
        CellStyle styleData = workbook.createCellStyle();
        styleData.setFillForegroundColor(IndexedColors.WHITE.index);
        styleData.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleData.setBorderBottom(BorderStyle.THIN);
        styleData.setBorderLeft(BorderStyle.THIN);
        styleData.setBorderRight(BorderStyle.THIN);
        styleData.setBorderTop(BorderStyle.THIN);
        styleData.setAlignment(HorizontalAlignment.CENTER);
        styleData.setVerticalAlignment(VerticalAlignment.CENTER);

        for (ExportDataDTO exportDataDTO : exportDataDTOList) {
            if (StringUtils.isEmpty(exportDataDTO.getData())) {
                throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "参数异常");
            }
            try {
                // 导出每页限制10W
                if (exportDataDTO.getData().size() > LIMIT_SIZE) {
                    exportDataDTO.setData(exportDataDTO.getData().subList(0, LIMIT_SIZE));
                }
                int sheetCount = getSheetCount(exportDataDTO.getData().size(), SHEET_SIZE);
                int firstIndex = 0;
                int lastIndex = 0;

                String sheetName = exportDataDTO.getSheetName();
                if (sheetCount == 0) {
                    firstIndex = 0;
                    lastIndex = exportDataDTO.getData().size() - 1;
                    fillSheet(workbook,excelType,exportDataDTO.getData(), exportDataDTO.getFieldMap(), firstIndex, lastIndex,
                            styleTitle, styleData, sheetName, "yyyy-MM-dd HH:mm:ss", exportDataDTO.getEnField());
                } else {
                    for (int i = 0; i < sheetCount; i++) {
                        if (i == sheetCount - 1) {
                            firstIndex = SHEET_SIZE * i;
                            lastIndex = exportDataDTO.getData().size() - 1;
                        } else {
                            firstIndex = SHEET_SIZE * i;
                            lastIndex = SHEET_SIZE * (i + 1) - 1;
                        }
                        fillSheet(workbook,excelType,exportDataDTO.getData(), exportDataDTO.getFieldMap(), firstIndex, lastIndex,
                                styleTitle, styleData, sheetCount == 1 ? sheetName : sheetName + "(" + i + ")",
                                "yyyy-MM-dd HH:mm:ss", exportDataDTO.getEnField());
                    }
                }
            } catch (Exception e) {
                throw e;
            }
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        return os.toByteArray();
    }

    private static int getSheetCount(int listSize, int sheetSize) {
        int result = listSize / sheetSize;

        if (listSize % sheetSize > 0) {
            result += 1;
        }
        return result;
    }

    /**
     * 向工作表中填充数据
     *
     * @param workbook   工作表
     * @param list       数据源
     * @param fieldMap   中英文字段对应关系的Map
     * @param firstIndex 开始索引
     * @param lastIndex  结束索引
     * @param styleTitle 表头样式
     * @param styleData  表内容样式
     * @param title      标题
     * @param format     日期转化为字符串的格式
     * @param enField    需要特殊转换列的英文名和对应的转换规则，不需要设为null
     * @throws Exception
     */
    private static <T> void fillSheet(Workbook workbook, ExcelType excelType,List<Map<String, Object>> list,
                                      LinkedHashMap<String, String> fieldMap, int firstIndex, int lastIndex, CellStyle styleTitle,
                                      CellStyle styleData, String title, String format, Map<String, Map<Object, String>> enField)
            throws Exception {
        Sheet sheet = workbook.createSheet(title);
        // 定义存放英文字段名和中文字段名的数组
        String[] enFields = new String[fieldMap.size()];
        String[] cnFields = new String[fieldMap.size()];

        // 填充数组
        int count = 0;
        for (Entry<String, String> entry : fieldMap.entrySet()) {
            enFields[count] = entry.getKey();
            cnFields[count] = entry.getValue();
            count++;
        }

        // 填充表头
        Row row = sheet.createRow(0);
        int index = 0;
        for (int i = 0; i < cnFields.length; i++) {
            // 一个字占800
            int width = cnFields[i].length() * 800;
            if (width < 5000) {
                width = 5000;
            }
            sheet.setColumnWidth(index, width);
            Cell cell = row.createCell(index++, CellType.STRING);
            cell.setCellStyle(styleTitle);
            RichTextString text = null;
            switch (excelType){
                case HSSF:
                    text=new HSSFRichTextString(cnFields[i]);
                    break;
                case XSSF:
                    text=new XSSFRichTextString(cnFields[i]);
                    break;
            }
            cell.setCellValue(text);
        }

        // 填充内容
        int rowNo = 1;
        for (index = firstIndex; index <= lastIndex; index++) {
            row = sheet.createRow(rowNo++);
            // 获取单个对象
            Map<String, Object> item = list.get(index);

            for (int i = 0; i < enFields.length; i++) {
                Cell cell = row.createCell(i, CellType.STRING);
                Object objValue = getFieldValueByName(enFields[i], item, format, enField);
                if (objValue instanceof String) {
                    DataFormat formatDate = workbook.createDataFormat();
                    styleData.setDataFormat(formatDate.getFormat("@"));
                }
                cell.setCellStyle(styleData);
                String fieldValue = (objValue == null ? "" : objValue.toString());
                RichTextString text = null;
                switch (excelType){
                    case HSSF:
                        text=new HSSFRichTextString(fieldValue);
                        break;
                    case XSSF:
                        text=new XSSFRichTextString(fieldValue);
                        break;
                }
                cell.setCellValue(text);
            }
        }
    }

    /**
     * 根据带路径或不带路径的属性名获取属性值,即接受简单属性名，如userName等，又接受带路径的属性名，如student.department.
     * name等,如果有日期类型则按照给定格式转化为字符串,并可以按照特殊格式转换填充内容
     *
     * @param fieldName 带路径的属性名或简单属性名
     * @param o         对象
     * @param format    日期转化为字符串的格式
     * @param enField   需要特殊转换列的英文名和对应的转换规则，不需要设为null
     * @return 属性值
     * @throws Exception
     */
    private static Object getFieldValueByName(String fieldName, Map<String, Object> o, String format,
                                              Map<String, Map<Object, String>> enField) throws Exception {

        Object value = o.get(fieldName);
        if (enField != null) {
            Map<Object, String> mapValue = enField.get(fieldName);
            if (mapValue != null) {
                Object v = mapValue.get(value);
                if (v != null) {
                    value = v;
                }
            }
        }
        if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            value = sdf.format(value);
        }

        return value;
    }
}
