package com.yiling.order.order.dto.request;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;
/**
 *
 *@author:tingwei.chen
 *@date:2021/6/22
 *
 **/
@Data
@Accessors(chain = true)
public class DamageOrderDetailRequest {


    /**
     * 订单明细detailId
     */
    private Long detailId;

    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 退款金额
     */
    private BigDecimal returnAmount;

    /**
     * 现折金额
     */
    private BigDecimal cashDiscountAmount;

    /**
     * 票折金额
     */
    private BigDecimal ticketDiscountAmount;
}
