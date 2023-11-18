package com.yiling.b2b.admin.settlement.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
 * @date 2021-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("对账单-订单明细")
public class SettlementOrderDetailPageVO extends BaseVO {

	/**
	 * 订单号
	 */
	@ApiModelProperty(value = "订单号")
	private String orderNo;

	/**
	 * 支付类型：1-线下支付 2-在线支付
	 */
	@ApiModelProperty(value = "支付类型")
	private Integer paymentType;

	/**
	 * 支付方式：1-线下支付 2-账期 3-预付款
	 */
	@ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
	private Integer paymentMethod;

	/**
	 * 在线支付渠道
	 */
	@ApiModelProperty(value = "在线支付渠道：wxPay-微信 aliPay-支付宝 yeePay-易宝")
	private String payChannel;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private Date orderCreateTime;


	/**
	 * 货款结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败
	 */
	@ApiModelProperty(value = "货款结算状态")
	private Integer goodsStatus;

	/**
	 * 货款结算单生成时间
	 */
	@ApiModelProperty(value = "货款结算单生成时间")
	private Date goodsCreateTime;

	/**
	 * 货款结算时间
	 */
	@ApiModelProperty(value = "货款结算时间")
	private Date goodsSettlementTime;

	/**
	 * 促销结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败
	 */
	@ApiModelProperty(value = "促销结算状态")
	private Integer saleStatus;

	/**
	 * 促销结算单生成时间
	 */
	@ApiModelProperty(value = "促销结算单生成时间")
	private Date saleCreateTime;

	/**
	 * 促销结算时间
	 */
	@ApiModelProperty(value = "促销结算时间")
	private Date saleSettlementTime;

    /**
     * 预售结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败
     */
    @ApiModelProperty(value = "预售结算状态")
    private Integer presaleDefaultStatus;

    /**
     * 预售结算单生成时间
     */
    @ApiModelProperty(value = "预售结算单生成时间")
    private Date preSaleCreateTime;

    /**
     * 预售结算时间
     */
    @ApiModelProperty(value = "预售结算时间")
    private Date preSaleSettlementTime;

    /**
     * 买家名称
     */
    @ApiModelProperty(value = "采购商")
    private String buyerEname;

    /**
     * 卖家名称
     */
    @ApiModelProperty(value = "供应商")
    private String sellerEname;

	/**
	 * 货款金额
	 */
	@ApiModelProperty(value = "货款金额")
	private BigDecimal goodsAmount;

	/**
	 * 货款退款金额
	 */
	@ApiModelProperty(value = "货款退款金额")
	private BigDecimal refundGoodsAmount;

	/**
	 * 货款结算金额
	 */
	@ApiModelProperty(value = "货款结算金额",hidden = true)
	@JsonIgnore
	private BigDecimal goodsSettlementAmount;

	/**
	 * 优惠券金额
	 */
	@ApiModelProperty(value = "优惠券金额")
	private BigDecimal couponAmount;

	/**
	 * 优惠券退款金额
	 */
	@ApiModelProperty(value = "优惠券退款金额")
	private BigDecimal refundCouponAmount;

	/**
	 * 满赠金额
	 */
	@ApiModelProperty(value = "满赠金额")
	private BigDecimal giftAmount;

	/**
	 * 满赠退款金额
	 */
	@ApiModelProperty(value = "满赠退款金额")
	private BigDecimal refundGiftAmount;

	/**
	 * 秒杀特价金额
	 */
	@ApiModelProperty(value = "秒杀特价金额")
	private BigDecimal promotionAmount;

	/**
	 * 秒杀特价退款金额
	 */
	@ApiModelProperty(value = "秒杀特价退款金额")
	private BigDecimal refundPromotionAmount;

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
    @ApiModelProperty(value = "预售优惠总金额-预售促销优惠金额")
    private BigDecimal presaleDiscountAmount;

    /**
     * 预售优惠退款金额
     */
    @ApiModelProperty(value = "预售优惠退款金额-退回预售促销优惠的金额")
    private BigDecimal refundPreAmount;

    /**
     * 支付促销优惠金额
     */
    @ApiModelProperty(value = "支付促销优惠金额")
    private BigDecimal payDiscountAmount;

    /**
     * 支付促销退款金额
     */
    @ApiModelProperty(value = "支付促销退款金额")
    private BigDecimal refundPayAmount;

	/**
	 * 促销结算金额
	 */
	@ApiModelProperty(value = "促销结算金额",hidden = true)
	@JsonIgnore
	private BigDecimal saleSettlementAmount;

    /**
     * 预售违约总金额
     */
    @ApiModelProperty(value = "预售违约总金额")
    private BigDecimal presaleDefaultAmount;

    /**
     * 预售违约退款金额
     */
    @ApiModelProperty(value = "预售违约退款金额")
    private BigDecimal refundPresaleDefaultAmount=new BigDecimal("0");

	/**
	 * 结算总金额
	 */
	@ApiModelProperty(value = "结算总金额")
	private BigDecimal totalSettlementAmount;

	/**
	 * 订单商品明细
	 */
	@ApiModelProperty(value = "订单商品明细")
	List<OrderDetailListItemVO> orderDetailList;

	public BigDecimal getTotalSettlementAmount() {
		BigDecimal subSaleSettlementAmount;
		subSaleSettlementAmount=ObjectUtil.isNull(saleSettlementAmount)?BigDecimal.ZERO:saleSettlementAmount;
		BigDecimal subGoodsSettlementAmount;
		subGoodsSettlementAmount=ObjectUtil.isNull(goodsSettlementAmount)?BigDecimal.ZERO:goodsSettlementAmount;
        BigDecimal pdSettAmount;
        pdSettAmount=ObjectUtil.isNull(presaleDefaultAmount)?BigDecimal.ZERO:presaleDefaultAmount;
        BigDecimal totalAmount = subSaleSettlementAmount.add(subGoodsSettlementAmount).add(pdSettAmount);
		return totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}


	public BigDecimal getGoodsAmount() {
		if (ObjectUtil.isNull(goodsAmount)){
			return BigDecimal.ZERO;
		}
		return goodsAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getRefundGoodsAmount() {
		if (ObjectUtil.isNull(refundGoodsAmount)){
			return BigDecimal.ZERO;
		}
		return refundGoodsAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getCouponAmount() {
		if (ObjectUtil.isNull(couponAmount)){
			return BigDecimal.ZERO;
		}
		return couponAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getRefundCouponAmount() {
		if (ObjectUtil.isNull(refundCouponAmount)){
			return BigDecimal.ZERO;
		}
		return refundCouponAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getGiftAmount() {
		if (ObjectUtil.isNull(giftAmount)){
			return BigDecimal.ZERO;
		}
		return giftAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getRefundGiftAmount() {
		if (ObjectUtil.isNull(refundGiftAmount)){
			return BigDecimal.ZERO;
		}
		return refundGiftAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getPromotionAmount() {
		if (ObjectUtil.isNull(promotionAmount)){
			return BigDecimal.ZERO;
		}
		return promotionAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getRefundPromotionAmount() {
		if (ObjectUtil.isNull(refundPromotionAmount)){
			return BigDecimal.ZERO;
		}
		return refundPromotionAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
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

    public BigDecimal getRefundPreAmount() {
        if (ObjectUtil.isNull(refundPreAmount)){
            return BigDecimal.ZERO;
        }
        return refundPreAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPresaleDefaultAmount() {
        if (ObjectUtil.isNull(presaleDefaultAmount)){
            return BigDecimal.ZERO;
        }
        return presaleDefaultAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPayDiscountAmount() {
        if (ObjectUtil.isNull(payDiscountAmount)){
            return BigDecimal.ZERO;
        }
        return payDiscountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getRefundPayAmount() {
        if (ObjectUtil.isNull(refundPayAmount)){
            return BigDecimal.ZERO;
        }
        return refundPayAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
