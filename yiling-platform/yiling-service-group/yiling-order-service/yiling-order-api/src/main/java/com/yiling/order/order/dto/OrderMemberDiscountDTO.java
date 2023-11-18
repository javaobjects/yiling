package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 会员订单优惠信息
 */
@Data
public class OrderMemberDiscountDTO  implements java.io.Serializable  {
    /**
     * 订单Id
     */
    private Long id;

    /**
     * 卖家企业ID
     */
    private Long sellerEid;

    /**
     * 卖家企业名称
     */
    private String sellerEname;

    /**
     * 商品件数
     */
    private Integer goodsQuantity;

    /**
     * 优惠金额
     */
    private BigDecimal DiscountAmount;

    /**
     * 下单时间
     */
    private Date createTime;
}
