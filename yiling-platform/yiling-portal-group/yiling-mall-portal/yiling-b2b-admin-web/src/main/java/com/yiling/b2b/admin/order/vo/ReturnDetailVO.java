package com.yiling.b2b.admin.order.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 退货单明细表前端返回实体
 *
 * @author: yong.zhang
 * @date: 2022/2/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReturnDetailVO extends BaseVO {

    @ApiModelProperty(value = "退货单ID")
    private Long returnId;

    @ApiModelProperty(value = "订单明细ID")
    private Long detailId;

    @ApiModelProperty(value = "商品标准库ID")
    private Long standardId;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品skuId")
    private Long goodsSkuId;

    @ApiModelProperty(value = "商品ERP编码")
    private String goodsErpCode;

    @ApiModelProperty(value = "退货数量")
    private Integer returnQuantity;

    @ApiModelProperty(value = "退货商品小计")
    private BigDecimal returnAmount;

    @ApiModelProperty(value = "退货商品的现折金额")
    private BigDecimal returnCashDiscountAmount;

    @ApiModelProperty(value = "退货商品的票折金额")
    private BigDecimal returnTicketDiscountAmount;

    @ApiModelProperty(value = "平台优惠劵折扣金额")
    private BigDecimal returnPlatformCouponDiscountAmount;

    @ApiModelProperty(value = "商家优惠劵折扣金额")
    private BigDecimal returnCouponDiscountAmount;

    @ApiModelProperty(value = "备注")
    private String remark;
}
