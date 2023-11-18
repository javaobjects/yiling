package com.yiling.dataflow.flow.excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/2/28
 */
@Data
public class GoodsBatchFlowExcel implements Serializable {

    private static final long serialVersionUID = -4672345329942342009L;

    @Excel(name = "日期",orderNum = "0",exportFormat = "yyyy-MM-dd")
    protected Date dateTime;

    @Excel(name = "产品名称",orderNum = "1")
    private String gbName;

    @Excel(name = "规格",orderNum = "2")
    private String gbSpecifications;

    @Excel(name = "批号",orderNum = "3")
    private String gbBatchNo;

    @Excel(name = "数量",orderNum = "4")
    private String gbNumber;

    @Excel(name = "单位",orderNum = "5")
    private String gbUnit;

    @Excel(name = "单价",orderNum = "6")
    private BigDecimal gbPrice;

    @Excel(name = "金额",orderNum = "7")
    private BigDecimal gbTotalPrice;

    @Excel(name = "入库日期",orderNum = "8",exportFormat = "yyyy-MM-dd")
    protected Date gbTime;

    @Excel(name = "生产日期",orderNum = "9",exportFormat = "yyyy-MM-dd")
    private Date gbProduceTime;

    @Excel(name = "有效期",orderNum = "10",exportFormat = "yyyy-MM-dd")
    private Date gbEndTime;

    @Excel(name = "生产厂家",orderNum = "11")
    private String gbManufacturer;

}
