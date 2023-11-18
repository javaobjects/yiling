package com.yiling.dataflow.statistics;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/10/19
 */
@Data
public class FlowExcel implements Serializable {
    private static final long serialVersionUID = -4672345329942342009L;

    @Excel(name = "年份(计入)",orderNum = "0")
    protected String     year;
    @Excel(name = "月份(计入)",orderNum = "1")
    protected String     month;
    @Excel(name = "商业名称",orderNum = "2")
    protected String     ename;
    @Excel(name = "产品品种",orderNum = "3")
    protected String     breed;
    @Excel(name = "产品编码",orderNum = "4")
    protected String     crmId;
    @Excel(name = "产品品名",orderNum = "5")
    protected String     goodsName;
    @Excel(name = "批次号",orderNum = "6")
    protected String     batchNo;
    @Excel(name = "产品考核价",orderNum = "7")
    protected BigDecimal sxPrice;

    @Excel(name = "上月存/盒",orderNum = "8")
    protected BigDecimal     lastNumber;
    @Excel(name = "本月进/盒",orderNum = "9")
    protected BigDecimal     purchaseNumber;
    @Excel(name = "本月销/盒",orderNum = "10")
    protected BigDecimal     saleNumber;
    @Excel(name = "本月实际库存/盒",orderNum = "11")
    protected BigDecimal     number;
    @Excel(name = "本月在途库存/盒",orderNum = "12")
    protected BigDecimal     onWayNumber;
    @Excel(name = "本月库存合计/盒",orderNum = "13")
    protected BigDecimal     totalNumber;
    @Excel(name = "计算本月存/盒",orderNum = "14")
    protected BigDecimal     calculationNumber;
    @Excel(name = "库存差异/盒",orderNum = "15")
    protected BigDecimal     diffNumber;
    @Excel(name = "上月存/元",orderNum = "16")
    protected BigDecimal     lastNumberAmount;
    @Excel(name = "本月进/元",orderNum = "17")
    protected BigDecimal     purchaseNumberAmount;
    @Excel(name = "本月销/元",orderNum = "18")
    protected BigDecimal     saleNumberAmount;
    @Excel(name = "本月实际库存/元",orderNum = "19")
    protected BigDecimal     numberAmount;
    @Excel(name = "本月在途库存/元",orderNum = "20")
    protected BigDecimal     onWayNumberAmount;
    @Excel(name = "本月库存合计/元",orderNum = "21")
    protected BigDecimal     totalNumberAmount;
    @Excel(name = "计算本月存/元",orderNum = "22")
    protected BigDecimal     calculationNumberAmount;
    @Excel(name = "库存差异/元",orderNum = "23")
    protected BigDecimal     diffNumberAmount;

}
