package com.yiling.order.order.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CountContinueOrderAndReturnDTO implements Serializable {

    /**
     * 订单数量
     */
    private Integer orderContinueCount;

    /**
     * 退货单数量
     */
    private Integer orderReturnContinueCount;

    /**
     * 销售订单
     */
    private Integer sellerOrderContinueCount;

}
