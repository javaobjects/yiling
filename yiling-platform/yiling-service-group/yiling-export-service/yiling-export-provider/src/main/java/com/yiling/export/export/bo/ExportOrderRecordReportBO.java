package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 数据统计报表
 * @author:wei.wang
 * @date:2021/11/10
 */
@Data
public class ExportOrderRecordReportBO {
    /**
     * 省份
     */
    private String provinceName;

    /**
     * 购进交易额
     */
    private BigDecimal buyAmount;

    /**
     * 已付款金额
     */
    private BigDecimal paymentAmount;


}
