package com.yiling.admin.b2b.settlement.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("订单明细page")
public class OrderDetailListItemVO extends BaseVO {

    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID",hidden = true)
    @JsonIgnore
    private Long orderId;

	/**
	 * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
	 */
	@ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款 4-在线支付",hidden = true)
	@JsonIgnore
	private Integer paymentMethod;

	/**
	 * 平台优惠劵折扣金额
	 */
	@ApiModelProperty(value = "平台优惠劵折扣金额",hidden = true)
	@JsonIgnore
	private BigDecimal platformCouponDiscountAmount;

	/**
	 * 商家优惠劵折扣金额
	 */
	@ApiModelProperty(value = "商家优惠劵折扣金额",hidden = true)
	@JsonIgnore
	private BigDecimal couponDiscountAmount;

	/**
	 * 商品小计
	 */
	@ApiModelProperty(value = "商品小计",hidden = true)
	@JsonIgnore
	private BigDecimal goodsAmount;

	/**
	 * 退货商品的平台优惠劵折扣金额
	 */
	@ApiModelProperty(value = "退货商品的平台优惠劵折扣金额",hidden = true)
	@JsonIgnore
	private BigDecimal returnPlatformCouponDiscountAmount;

	/**
	 * 退货商品的商家优惠劵折扣金额
	 */
	@ApiModelProperty(value = "退货商品的商家优惠劵折扣金额",hidden = true)
	@JsonIgnore
	private BigDecimal returnCouponDiscountAmount;

	/**
	 * 退货商品总小计
	 */
	@ApiModelProperty(value = "退货商品总小计",hidden = true)
	@JsonIgnore
	private BigDecimal returnAmount;

	/**
	 * 平台优惠劵平台承担比例
	 */
	@ApiModelProperty(value = "平台优惠劵平台承担比例",hidden = true)
	@JsonIgnore
	private BigDecimal platformRatio;

	/**
	 * 商家券平台承担比例
	 */
	@ApiModelProperty(value = "商家券平台承担比例",hidden = true)
	@JsonIgnore
	private BigDecimal shopPlatformRatio;

	/**
	 * 促销活动ID(折扣价活动预留)
	 */
	@ApiModelProperty(value = "促销活动ID",hidden = true)
	@JsonIgnore
	private Long promotionActivityId;

    /**
     * 促销活动类型
     * @see com.yiling.order.order.enums.PromotionActivityTypeEnum
     */
    @ApiModelProperty(value = "促销活动类型",hidden = true)
    @JsonIgnore
    private Integer promotionActivityType;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String goodsName;

	/**
	 * 商品规格
	 */
	@ApiModelProperty(value = "商品规格")
	private String goodsSpecification;

	/**
	 * 商品生产厂家
	 */
	@ApiModelProperty(value = "商品生产厂家")
	private String goodsManufacturer;

	/**
	 * 购买数量
	 */
	@ApiModelProperty(value = "购买数量")
	private Integer goodsQuantity;

	/**
	 * 发货数量
	 */
	@ApiModelProperty(value = "发货数量")
	private Integer deliveryQuantity;

	/**
	 * 收货数量
	 */
	@ApiModelProperty(value = "收货数量")
	private Integer receiveQuantity;

	/**
	 * 货款金额
	 */
	@ApiModelProperty(value = "货款金额--结算单类型为“货款单”或“订单对账的订单明细列表”时货款小计或货款金额取该值")
	private BigDecimal goodsTotalAmount;

	/**
	 * 货款退款金额
	 */
	@ApiModelProperty(value = "货款退款金额--结算单类型为“货款单”或“订单对账的订单明细列表”时退款小计或货款退款金额取该值")
	private BigDecimal goodsRefundAmount;

	/**
	 * 货款结算金额
	 */
	@ApiModelProperty(value = "货款结算金额--结算单类型为货款单时结算小计取该值,当查询订单对账的订单明细列表时该字段无意义")
	private BigDecimal goodsSettlementAmount;

	/**
	 * 优惠券金额
	 */
	@ApiModelProperty(value = "优惠券金额--结算单类型为“促销单”或“订单对账的订单明细列表”时优惠券金额取该值")
	private BigDecimal couponAmount;

	/**
	 * 优惠券退款金额
	 */
	@ApiModelProperty(value = "优惠券退款金额--结算单类型为“促销单”或“订单对账的订单明细列表”时优惠券退款或优惠券退款金额取该值")
	private BigDecimal couponRefundAmount;

    /**
     * 秒杀特价金额
     */
    @ApiModelProperty(value = "秒杀特价金额")
    private BigDecimal promotionSaleSubTotal;

    /**
     * 退货促销活动优惠小计
     */
    @ApiModelProperty(value = "秒杀特价退货金额")
    private BigDecimal returnPromotionSaleSubTotal;

    /**
     * 组合促销优惠金额
     */
    @ApiModelProperty(value = "套装总金额-组合促销优惠金额")
    private BigDecimal comPacAmount;

    /**
     * 退回组合促销优惠的金额
     */
    @ApiModelProperty(value = "套装退款金额-退回组合促销优惠的金额")
    private BigDecimal returnComPacAmount;

    /**
     * 预售优惠金额
     */
    @ApiModelProperty(value = "预售优惠金额-预售促销优惠金额")
    private BigDecimal presaleDiscountAmount;

    /**
     * 退货商品预售金额
     */
    @ApiModelProperty(value = "预售退款金额-退回预售促销优惠的金额")
    private BigDecimal returnPresaleDiscountAmount;

    /**
     * 平台支付促销优惠金额
     */
    @ApiModelProperty(value = "平台支付促销优惠金额")
    private BigDecimal paymentPlatformDiscountAmount;

    /**
     * 商家支付促销优惠金额
     */
    @ApiModelProperty(value = "商家支付促销优惠金额")
    private BigDecimal paymentShopDiscountAmount;

    /**
     * 退货商品的平台支付优惠金额
     */
    @ApiModelProperty(value = "退货商品支付促销金额")
    private BigDecimal returnPlatformPaymentDiscountAmount;

    /**
     * 退货商品的商家支付优惠金额
     */
    @JsonIgnore
    @ApiModelProperty(value = "退货商品支付促销金额",hidden = true)
    private BigDecimal returnShopPaymentDiscountAmount;

	/**
	 * 促销结算金额
	 */
	@ApiModelProperty(value = "促销结算金额--结算单类型为促销单时结算小计取该值,当查询订单对账的订单明细列表时该字段无意义")
	private BigDecimal saleSettlementAmount;

    /**
     * 预售违约金额
     */
    @ApiModelProperty(value = "预售违约金额")
    private BigDecimal presaleDefaultAmount;

    /**
     * 预售违约金退款金额
     */
    @ApiModelProperty(value = "预售违约金退款金额")
    private BigDecimal refundPresaleDefaultAmount;

    /**
     * 预售违约结算金额
     */
    @ApiModelProperty(value = "预售违约结算金额--结算单类型为预售违约结算单时结算小计取该值,当查询订单对账的订单明细列表时该字段无意义")
    private BigDecimal preDefSettlementAmount;


	/**
	 * 结算小计
	 */
	@ApiModelProperty(value = "结算小计--仅当查询订单对账的订单明细列表时结算小计取该值")
	private BigDecimal totalAmount;

	public BigDecimal getTotalAmount() {
		if (ObjectUtil.isNull(goodsSettlementAmount) ){
			goodsSettlementAmount=BigDecimal.ZERO;
		}
		if (ObjectUtil.isNull(saleSettlementAmount) ){
			saleSettlementAmount=BigDecimal.ZERO;
		}
		if (ObjectUtil.isNull(preDefSettlementAmount) ){
            preDefSettlementAmount=BigDecimal.ZERO;
		}
		BigDecimal amount=goodsSettlementAmount.add(saleSettlementAmount).add(preDefSettlementAmount);
		return amount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getSaleSettlementAmount() {
		if (ObjectUtil.isNull(saleSettlementAmount)){
			return BigDecimal.ZERO;
		}
		return saleSettlementAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getGoodsSettlementAmount() {
		if (ObjectUtil.isNull(goodsSettlementAmount)){
			return BigDecimal.ZERO;
		}
		return goodsSettlementAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getGoodsTotalAmount() {
		if (ObjectUtil.isNull(goodsTotalAmount)){
			return BigDecimal.ZERO;
		}
		return goodsTotalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getCouponAmount() {
		if (ObjectUtil.isNull(couponAmount)){
			return BigDecimal.ZERO;
		}
		return couponAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getCouponRefundAmount() {
		if (ObjectUtil.isNull(couponRefundAmount)){
			return BigDecimal.ZERO;
		}
		return couponRefundAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getGoodsRefundAmount() {
		if (ObjectUtil.isNull(goodsRefundAmount)){
			return BigDecimal.ZERO;
		}
		return goodsRefundAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getPromotionSaleSubTotal() {
		if (ObjectUtil.isNull(promotionSaleSubTotal)){
			return BigDecimal.ZERO;
		}
		return promotionSaleSubTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getReturnPromotionSaleSubTotal() {
		if (ObjectUtil.isNull(returnPromotionSaleSubTotal)){
			return BigDecimal.ZERO;
		}
		return returnPromotionSaleSubTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

    public BigDecimal getComPacAmount() {
        if (ObjectUtil.isNull(comPacAmount)){
            return BigDecimal.ZERO;
        }
        return comPacAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getReturnComPacAmount() {
        if (ObjectUtil.isNull(returnComPacAmount)){
            return BigDecimal.ZERO;
        }
        return returnComPacAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPresaleDiscountAmount() {
        if (ObjectUtil.isNull(presaleDiscountAmount)){
            return BigDecimal.ZERO;
        }
        return presaleDiscountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getReturnPresaleDiscountAmount() {
        if (ObjectUtil.isNull(returnPresaleDiscountAmount)){
            return BigDecimal.ZERO;
        }
        return returnPresaleDiscountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPresaleDefaultAmount() {
        if (ObjectUtil.isNull(presaleDefaultAmount)){
            return BigDecimal.ZERO;
        }
        return presaleDefaultAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getRefundPresaleDefaultAmount() {
        if (ObjectUtil.isNull(refundPresaleDefaultAmount)){
            return BigDecimal.ZERO;
        }
        return refundPresaleDefaultAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPreDefSettlementAmount() {
        if (ObjectUtil.isNull(preDefSettlementAmount)){
            return BigDecimal.ZERO;
        }
        return preDefSettlementAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPaymentPlatformDiscountAmount() {
        if (ObjectUtil.isNull(paymentPlatformDiscountAmount)){
            return BigDecimal.ZERO;
        }
        return paymentPlatformDiscountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPaymentShopDiscountAmount() {
        if (ObjectUtil.isNull(paymentShopDiscountAmount)){
            return BigDecimal.ZERO;
        }
        return paymentShopDiscountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getReturnPlatformPaymentDiscountAmount() {
        if (ObjectUtil.isNull(returnPlatformPaymentDiscountAmount)){
            return BigDecimal.ZERO;
        }
        return returnPlatformPaymentDiscountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getReturnShopPaymentDiscountAmount() {
        if (ObjectUtil.isNull(returnShopPaymentDiscountAmount)){
            return BigDecimal.ZERO;
        }
        return returnShopPaymentDiscountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
