package com.yiling.open.math;

import javax.validation.constraints.NotEmpty;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/2/28
 */
@Data
public class ErpCustomerExcel{

    private static final long serialVersionUID = -4672345329942342009L;

    @Excel(name = "许可证编号")
    private String no;

    @Excel(name = "企业名称")
    protected String name;

    @Excel(name = "地址")
    private String address;

    @Excel(name = "经营方式")
    private String type;

    @Excel(name = "统一信用代码")
    private String licenseNumber;
}
