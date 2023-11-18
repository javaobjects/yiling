package com.yiling.export.imports.model;

import javax.validation.constraints.NotEmpty;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yiling.export.excel.model.EasyExcelBaseModel;
import com.yiling.framework.common.annotations.ExcelShow;
import lombok.Data;

/**
 * 导入月流向销售 Form
 *
 * @author: lun.yu
 * @date: 2023-03-04
 */
@Data
public class ImportFlowMonthSalesModel extends EasyExcelBaseModel {

    @ExcelShow
    @ExcelProperty(value = "销售日期", index = 0)
    @NotEmpty(message = "不能为空")
    private String soTimeStr;

    @ExcelShow
    @ExcelProperty(value = "产品名称", index = 1)
    @NotEmpty(message = "不能为空")
    private String goodsName;

    @ExcelShow
    @ExcelProperty(value = "产品规格", index = 2)
    @NotEmpty(message = "不能为空")
    private String soSpecifications;

    @ExcelProperty(value = "批号", index = 3)
    private String soBatchNo;

    @ExcelShow
    @ExcelProperty(value = "客户名称", index = 4)
    @NotEmpty(message = "不能为空")
    private String enterpriseName;

    @ExcelShow
    @ExcelProperty(value = "数量（盒）", index = 5)
    @NotEmpty(message = "不能为空")
    private String soQuantityStr;

    @ExcelProperty(value = "单位", index = 6)
    private String soUnit;

    @ExcelProperty(value = "单价", index = 7)
    private String soPrice;

    @ExcelProperty(value = "金额", index = 8)
    private String soTotalAmount;

    @ExcelProperty(value = "生产厂家", index = 9)
    private String soManufacturer;

    @ExcelProperty(value = "开票员", index = 10)
    private String soInvoiceClerk;

    @ExcelProperty(value = "业务员", index = 11)
    private String soSalesMan;

    @ExcelProperty(value = "客户地址", index = 12)
    private String customerAddress;

    @ExcelProperty(value = "客户城市", index = 13)
    private String customerCityName;

    @ExcelProperty(value = "客户区县", index = 14)
    private String customerRegionName;

    @ExcelProperty(value = "备注", index = 15)
    private String remark;

}
