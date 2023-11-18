package com.yiling.order.order.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 根据订单类型统计商品销量
 */
@Data
public class OrderTypeGoodsQuantityDTO implements java.io.Serializable  {

    /**
     * 下单数量
     */
    private Long goodsQuantity;

    /**
     * 配送商品ID
     */
    private Long distributorGoodsId;

    /**
     * 1-POP订单 2-B2B订单
     */
    private Integer orderType;

}
