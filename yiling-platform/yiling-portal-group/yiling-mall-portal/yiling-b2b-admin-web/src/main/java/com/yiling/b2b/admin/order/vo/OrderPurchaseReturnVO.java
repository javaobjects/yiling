package com.yiling.b2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 退货单
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderPurchaseReturnVO extends BaseVO {

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "退货单号")
    private String orderReturnNo;

    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
    private Integer paymentMethod;

    @ApiModelProperty(value = "供应商id")
    private Long sellerEid;

    @ApiModelProperty(value = "供应商名称")
    private String sellerEname;

    @ApiModelProperty(value = "采购商id")
    private Long buyerEid;

    @ApiModelProperty(value = "采购商名称")
    private String buyerEname;

    @ApiModelProperty(value = "退货单类型：1-供应商退货单 2-破损退货单 3-采购退货单")
    private Integer returnType;

    @ApiModelProperty(value = "退货单状态：1-待审核 2-审核通过 3-审核驳回")
    private Integer returnStatus;

    @ApiModelProperty(value = "退回货款金额", required = true)
    private BigDecimal returnGoodsAmount;

    @ApiModelProperty(value = "优惠总金额")
    private BigDecimal totalDiscountAmount;

    @ApiModelProperty(value = "实退金额")
    private BigDecimal returnAmount;

    @ApiModelProperty(value = "提交时间")
    private Date createTime;

    @ApiModelProperty(value = "退货商品件数")
    private Integer returnGoods;

    @ApiModelProperty(value = "退货商品总数量")
    private Integer returnGoodsNum;




}
