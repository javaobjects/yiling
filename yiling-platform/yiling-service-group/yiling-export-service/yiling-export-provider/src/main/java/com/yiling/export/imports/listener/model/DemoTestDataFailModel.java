package com.yiling.export.imports.listener.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yiling.export.excel.model.EasyExcelBaseFailModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
public class DemoTestDataFailModel extends EasyExcelBaseFailModel {
    @NotNull(message = "序号不能为null")
    @ExcelProperty(value = "序号")
    private Long id;
    @ExcelProperty(value = "药店代码")
    private String code;
    @ExcelProperty(value = "国家")
    private String guoJia;
    @ExcelProperty(value = "金额")
    private BigDecimal amonut;
}
