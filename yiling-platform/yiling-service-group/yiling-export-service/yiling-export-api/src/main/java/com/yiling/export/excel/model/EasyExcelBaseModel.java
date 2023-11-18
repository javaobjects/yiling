package com.yiling.export.excel.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.yiling.export.excel.handler.StatusConverter;
import lombok.Data;

@Data
public  class EasyExcelBaseModel {
    /**
     *  1 失败，2成功
     */
    @ExcelProperty(value = "状态",converter = StatusConverter.class)
    protected Integer status;

    @ExcelProperty(value = "失败信息")
    private String  errorMsg;

    @ExcelIgnore
    private Integer rowNum;
}
