package com.yiling.dataflow.report.dto;

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
public class ReportFlowPopPurchaseDTO extends BaseDTO {

    /**
     * 销售时间
     */
    private Date poTime;

    /**
     * 销售数量
     */
    private Long erpQuantity;

    /**
     * 销售总额
     */
    private BigDecimal erpTotalAmount;

    /**
     * 销售数量
     */
    private Long poQuantity;

    /**
     * 销售总额
     */
    private BigDecimal poTotalAmount;
}
