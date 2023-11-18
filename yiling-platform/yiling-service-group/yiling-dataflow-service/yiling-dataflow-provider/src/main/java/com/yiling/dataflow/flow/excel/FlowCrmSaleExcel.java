package com.yiling.dataflow.flow.excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.annotations.CsvOrder;

import lombok.Data;

/**
 * @author fucheng.bai
 * @date 2023/2/2
 */
@Data
public class FlowCrmSaleExcel implements Serializable {

    private static final long serialVersionUID = -6309062319779814801L;

    /**
     * 销售日期
     */
    @CsvOrder(value = 0)
    private String soTime;

    /**
     * 年份
     */
    @CsvOrder(value = 1)
    private String year;

    /**
     * 月份
     */
    @CsvOrder(value = 2)
    private String month;

    /**
     * 商业公司名称
     */
    @CsvOrder(value = 4)
    private String ename;

    /**
     * 流向客户名称
     */
    @CsvOrder(value = 21)
    private String enterpriseName;

    /**
     * crm产品代码
     */
    @CsvOrder(value = 51)
    private String crmGoodsCode;

    /**
     * crm产品名称
     */
    @CsvOrder(value = 52)
    private String crmGoodsName;

    /**
     * 数量
     */
    @CsvOrder(value = 55)
    private String soQuantity;

    /**
     * 批号
     */
    @CsvOrder(value = 58)
    private String soBatchNo;

}
