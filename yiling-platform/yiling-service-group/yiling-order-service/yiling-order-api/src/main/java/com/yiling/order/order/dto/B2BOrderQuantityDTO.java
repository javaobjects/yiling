package com.yiling.order.order.dto;

import lombok.Data;

/**
 * B2B移动端
 * @author:wei.wang
 * @date:2021/11/4
 */
@Data
public class B2BOrderQuantityDTO implements java.io.Serializable {

    /**
     * 待付款数量
     */
    private Integer unPaymentQuantity;

    /**
     * 待发货数量
     */
    private Integer unDeliveryQuantity;

    /**
     * 待收货数量
     */
    private Integer unReceiveQuantity;

    /**
     * 退货数量
     */
    private Integer returnQuantity;
}
