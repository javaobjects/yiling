package com.yiling.dataflow.flow.excel;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/2/28
 */
@Data
public class SaleFlowExcel implements Serializable {

    private static final long serialVersionUID = -1;

    @Excel(name = "销售日期",orderNum = "0")
    private String soTime;

    @Excel(name = "产品名称",orderNum = "1")
    private String goodsName;

    @Excel(name = "规格",orderNum = "2")
    private String soSpecifications;

    @Excel(name = "批号",orderNum = "3")
    private String soBatchNo;

    @Excel(name = "客户名称",orderNum = "4")
    private String enterpriseName;

    @Excel(name = "数量",orderNum = "5")
    private BigDecimal soQuantity;

    @Excel(name = "单位",orderNum = "6")
    private String soUnit;

    @Excel(name = "单价",orderNum = "7")
    private BigDecimal soPrice;

    @Excel(name = "金额",orderNum = "8")
    private BigDecimal soTotalPrice;

    @Excel(name = "生产厂家",orderNum = "9")
    private String soManufacturer;

    @Excel(name = "开票员",orderNum = "10")
    private String invoiceClerk;

    @Excel(name = "业务员",orderNum = "11")
    private String salesMan;

    @Excel(name = "客户地址",orderNum = "12")
    private String customerAddress;

    @Excel(name = "客户城市",orderNum = "13")
    private String customerCity;

    @Excel(name = "客户区县",orderNum = "14")
    private String customerCountry;

    @Excel(name = "备注",orderNum = "15")
    private String remark;

    @Excel(name = "申诉类型",orderNum = "16")
    private String appealType;
}
