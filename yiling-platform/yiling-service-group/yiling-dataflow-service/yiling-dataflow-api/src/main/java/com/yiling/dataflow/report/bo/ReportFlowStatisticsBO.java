package com.yiling.dataflow.report.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/6/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReportFlowStatisticsBO extends BaseDTO {

    /**
     * 供应商数量
     */
    private Integer count;

    /**
     * 销售总额
     */
    private BigDecimal amount;

    /**
     * 销售数量
     */
    private Long number;


    /**
     * 销售数量
     */
    private Long terminalNumber;
    /**
     * 销售总额
     */
    private BigDecimal terminalAmount;

    /**
     * 销售时间
     */
    private Date time;
}
