package com.yiling.export.export.service;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import cn.afterturn.easypoi.excel.export.styler.AbstractExcelExportStyler;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;

/** 大数据导出表头样式
 * @author zhigang.guo
 * @date: 2023/3/20
 */
public class ExcelExportBigDataStylerImpl extends AbstractExcelExportStyler implements IExcelExportStyler {

    @Override
    public CellStyle getHeaderStyle(short color) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        titleStyle.setFont(font);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);


        return titleStyle;
    }


    @Override
    public CellStyle getTitleStyle(short color) {

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


        return styleTitle;
    }

    @Override
    public CellStyle stringSeptailStyle(Workbook workbook, boolean isWarp) {

        CellStyle styleData = workbook.createCellStyle();
        styleData.setFillForegroundColor(IndexedColors.WHITE.index);
        styleData.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleData.setBorderBottom(BorderStyle.THIN);
        styleData.setBorderLeft(BorderStyle.THIN);
        styleData.setBorderRight(BorderStyle.THIN);
        styleData.setBorderTop(BorderStyle.THIN);
        styleData.setAlignment(HorizontalAlignment.CENTER);
        styleData.setVerticalAlignment(VerticalAlignment.CENTER);

        return styleData;
    }

    @Override
    public CellStyle stringNoneStyle(Workbook workbook, boolean isWarp) {
        CellStyle styleData = workbook.createCellStyle();
        styleData.setFillForegroundColor(IndexedColors.WHITE.index);
        styleData.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleData.setBorderBottom(BorderStyle.THIN);
        styleData.setBorderLeft(BorderStyle.THIN);
        styleData.setBorderRight(BorderStyle.THIN);
        styleData.setBorderTop(BorderStyle.THIN);
        styleData.setAlignment(HorizontalAlignment.CENTER);
        styleData.setVerticalAlignment(VerticalAlignment.CENTER);

        styleData.setDataFormat(STRING_FORMAT);
        if (isWarp) {
            styleData.setWrapText(true);
        }
        return styleData;
    }

    public ExcelExportBigDataStylerImpl(Workbook workbook) {

        super.createStyles(workbook);
    }

}
