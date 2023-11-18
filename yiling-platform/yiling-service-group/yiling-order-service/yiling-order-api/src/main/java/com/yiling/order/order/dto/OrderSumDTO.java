package com.yiling.order.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto
 * @date: 2021/9/27
 */
@Data
@Accessors(chain = true)
@Builder
public class OrderSumDTO implements Serializable {

    private static final long serialVersionUID=-7851140500516591200L;

    /**
     * 订单总金额
     */
    private BigDecimal totalOrderMoney;

    /**
     * 订单总数量(或者商品数量)
     */
    private Integer orderTotalNumer;

    /**
     * 订单支付总金额
     */
    private BigDecimal totalPayAmount;

}
