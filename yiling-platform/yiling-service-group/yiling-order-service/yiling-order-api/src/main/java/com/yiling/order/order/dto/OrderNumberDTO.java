package com.yiling.order.order.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 销售和采购订单详情DTO
 *
 * @author:wei.wang
 * @date:2021/6/18
 */
@Data
public class OrderNumberDTO implements java.io.Serializable {
    /**
     * 今天订单数量
     */
    private Integer todayOrderNum;

    /**
     * 昨天订单数量
     */
    private Integer yesterdayOrderNum;

    /**
     * 进一年数据
     */
    private Integer yearOrderNum;

    /**
     * 货款金额
     */
    private BigDecimal totalAmount;

    /**
     * 优惠总金额
     */
    private BigDecimal discountAmount;

    /**
     * 支付总金额
     */
    private BigDecimal paymentAmount;
}

