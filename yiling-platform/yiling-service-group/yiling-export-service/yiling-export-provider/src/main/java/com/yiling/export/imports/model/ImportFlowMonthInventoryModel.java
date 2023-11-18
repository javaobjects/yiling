package com.yiling.export.imports.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yiling.export.excel.model.EasyExcelBaseModel;
import com.yiling.framework.common.annotations.ExcelShow;

import lombok.Data;

/**
 * 导入月流向库存 Form
 *
 * @author: lun.yu
 * @date: 2023-03-04
 */
@Data
public class ImportFlowMonthInventoryModel extends EasyExcelBaseModel {

    @ExcelShow
    @ExcelProperty(value = "库存提取日期", index = 0)
    private String gbExtractTimeStr;

    @ExcelShow
    @ExcelProperty(value = "产品名称", index = 1)
    @NotEmpty(message = "不能为空")
    private String gbName;

    @ExcelShow
    @ExcelProperty(value = "产品规格", index = 2)
    @NotEmpty(message = "不能为空")
    private String gbSpecifications;

    @ExcelProperty(value = "批号", index = 3)
    private String gbBatchNo;

    @ExcelShow
    @ExcelProperty(value = "数量（盒）", index = 4)
    @NotEmpty(message = "不能为空")
    private String gbNumberStr;

    @ExcelProperty(value = "单位", index = 5)
    private String gbUnit;

    @ExcelProperty(value = "单价", index = 6)
    private String gbPrice;

    @ExcelProperty(value = "金额", index = 7)
    private String gbTotalAmount;

    @ExcelProperty(value = "入库日期", index = 8)
    private String gbTimeStr;

    @ExcelProperty(value = "生产日期", index = 9)
    private String gbProduceTime;

    @ExcelProperty(value = "有效期", index = 10)
    private String gbEndTime;

    @ExcelProperty(value = "生产厂家", index = 11)
    private String gbManufacturer;

}
