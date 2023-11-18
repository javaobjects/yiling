package com.yiling.export.imports.listener.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yiling.export.excel.model.EasyExcelBaseModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
public class DemoTestModel extends EasyExcelBaseModel {
    @NotNull(message = "序号不能为null")
    @ExcelProperty(index = 0,value = "序号")
    private Long id;
    @ExcelProperty(index = 1,value = "药店代码")
    private String code;
    @ExcelProperty(index = 9,value = "国家")
    private String guoJia;
    @ExcelProperty(index = 11,value = "金额")
    private BigDecimal amonut;
}
