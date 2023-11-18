package com.yiling.order.order.dto;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 *@author:tingwei.chen
 *@date:2021/6/23
 */
@Data
@Accessors(chain = true)
public class OrderReturnGoodsBatchDTO{

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单明细ID
     */
    private Long detailId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 有效期
     */
    private Date expiryDate;

    /**
     * 购买数量
     */
    private Integer goodsQuantity;

    /**
     * 发货数量
     */
    private Integer deliveryQuantity;

    /**
     * 收货数量
     */
    private Integer receiveQuantity;
    /**
     * 退货数量
     */
    private Integer returnQuantity;

}
