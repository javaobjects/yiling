package com.yiling.export.imports.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.export.excel.model.EasyExcelBaseModel;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 导入月流向采购 Form
 *
 * @author: lun.yu
 * @date: 2023-03-04
 */
@Data
public class ImportFlowMonthPurchaseModel extends EasyExcelBaseModel {

    @ExcelShow
    @ExcelProperty(value = "购进日期", index = 0)
    @NotEmpty(message = "不能为空")
    private String poTimeStr;

    @ExcelShow
    @ExcelProperty(value = "产品名称", index = 1)
    @NotEmpty(message = "不能为空")
    private String goodsName;

    @ExcelShow
    @ExcelProperty(value = "产品规格", index = 2)
    @NotEmpty(message = "不能为空")
    private String poSpecifications;

    @ExcelProperty(value = "批号", index = 3)
    private String poBatchNo;

    @ExcelShow
    @ExcelProperty(value = "供应商名称", index = 4)
    @NotEmpty(message = "不能为空")
    private String enterpriseName;

    @ExcelShow
    @ExcelProperty(value = "数量（盒）", index = 5)
    @NotEmpty(message = "不能为空")
    private String poQuantityStr;

    @ExcelProperty(value = "单位", index = 6)
    private String poUnit;

    @ExcelProperty(value = "单价", index = 7)
    private String poPrice;

    @ExcelProperty(value = "金额", index = 8)
    private String poTotalAmount;

    @ExcelProperty(value = "生产厂家", index = 9)
    private String poManufacturer;

    @ExcelProperty(value = "采购员", index = 10)
    private String poBuyer;

}
