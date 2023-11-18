package com.yiling.f2b.admin.enterprise.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * POP后台首页采购销售待处理事物
 * @author:wei.wang
 * @date:2021/8/10
 */
@Data
public class IndexOrderNumberVO {
    /**
     * 采购待支付订单数
     */
    @ApiModelProperty(value = "采购待支付订单数")
    private Integer buyerUnPaymentNum;

    /**
     * 采购待发货订单数
     */
    @ApiModelProperty(value = "采购待发货订单数")
    private Integer buyerUnDeliveryNum;

    /**
     * 采购待签收订单数
     */
    @ApiModelProperty(value = "采购待签收订单数")
    private Integer buyerUnReceiveNum;

    /**
     * 销售待发货订单数
     */
    @ApiModelProperty(value = "销售待发货订单数")
    private Integer sellerUnDeliveryNum;

    /**
     * 销售已发货订单
     */
    @ApiModelProperty(value = "销售已发货订单")
    private Integer sellerDeliveryNum;

    /**
     * 销售退货中订单
     */
    @ApiModelProperty(value = "销售退货中订单")
    private Integer sellerReturningNum;
}
