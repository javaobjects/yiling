package com.yiling.export.excel.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public  class EasyExcelBaseFailModel {
    //1 失败，2成功
    @ExcelIgnore
    protected Integer status;
    @ExcelProperty(value = "状态")
    protected String statusStr;
    @ExcelProperty(value = "错误原因")
    private String  errorMsg;
    @ExcelProperty(value = "数据行")
    private Integer rowNum;
    public String getStatusStr() {
        return status==1?"失败":"成功";
    }
}
