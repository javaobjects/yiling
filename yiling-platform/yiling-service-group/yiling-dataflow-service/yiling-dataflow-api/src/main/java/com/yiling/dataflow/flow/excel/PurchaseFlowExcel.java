package com.yiling.dataflow.flow.excel;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-09-23
 */
@Data
public class PurchaseFlowExcel implements Serializable {

    private static final long serialVersionUID = -4672345329942342009L;

    @Excel(name = "购进日期",orderNum = "0")
    private String poTime;

    @Excel(name = "产品名称",orderNum = "1")
    private String goodsName;

    @Excel(name = "规格",orderNum = "2")
    private String poSpecifications;

    @Excel(name = "批号",orderNum = "3")
    private String poBatchNo;

    @Excel(name = "供应商名称",orderNum = "4")
    private String enterpriseName;

    @Excel(name = "数量",orderNum = "5")
    private BigDecimal poQuantity;

    @Excel(name = "单位",orderNum = "6")
    private String poUnit;

    @Excel(name = "单价",orderNum = "7")
    private BigDecimal poPrice;

    @Excel(name = "金额",orderNum = "8")
    private BigDecimal poTotalPrice;

    @Excel(name = "生产厂家",orderNum = "9")
    private String poManufacturer;

}
