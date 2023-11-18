package com.yiling.b2b.admin.index.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商家后台首页订单数量
 * @author:wei.wang
 * @date:2021/11/16
 */
@Data
public class B2BOrderQuantityVO {

    /**
     * 待发货数量
     */
    @ApiModelProperty(value = "待发货数量")
    private Integer unDeliveryQuantity;

    /**
     * 待收货数量
     */
    @ApiModelProperty(value = "待收货数量")
    private Integer unReceiveQuantity;

    /**
     * 退货数量
     */
    @ApiModelProperty(value = "退货数量")
    private Integer returnQuantity;

    /**
     * 会员是否推广：0-否 1-是
     */
    @ApiModelProperty(value = "会员是否推广：0-否 1-是")
    private Integer memberPushShow;

    /**
     * 待付款数量
     */
    @ApiModelProperty(value = "采购订单待付款数量")
    private Integer unPurchasePaymentQuantity;

    /**
     * 待发货数量
     */
    @ApiModelProperty(value = "采购订待发货数量")
    private Integer unPurchaseDeliveryQuantity;

    /**
     * 待收货数量
     */
    @ApiModelProperty(value = "采购订待收货数量")
    private Integer unPurchaseReceiveQuantity;
}
