package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * B2B订单报表金额统计DTO
 * @author:wei.wang
 * @date:2021/11/03
 */
@Data
public class OrderB2BCountAmountDTO implements java.io.Serializable {

    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 原价金额
     */
    private BigDecimal originalAmount;
}
