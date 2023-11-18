package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author dexi.yao
 * @date 2021-11-04
 */
@Data
public class ExportB2bSettlementOrderBO {

	/**
	 * 结算单号
	 */
	private String code;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 买家id
	 */
	private Long buyerEid;

	/**
	 * 买家name
	 */
	private String buyerName;

	/**
	 * 客户ERP编码
	 */
	private String customerErpCode;

	/**
	 * 卖家企业id
	 */
	private Long sellerEid;

	/**
	 * 卖家企业name
	 */
	private String sellerName;

	/**
	 * 订单创建时间
	 */
	private Date orderCreateTime;

	/**
	 * 订单支付时间
	 */
	private Date paymentTime;

	/**
	 * 总货款金额
	 */
	private BigDecimal totalGoodsAmount;

	/**
	 * 退款的货款金额
	 */
	private BigDecimal refundGoodsAmount;

	/**
	 * 货款单创建时间
	 */
	private Date goodsCreateTime;

	/**
	 * 货款单结算时间
	 */
	private Date goodsSettlementTime;

	/**
	 * 平台承担券金额
	 */
	private BigDecimal couponAmount;

	/**
	 * 平台承担券退款金额
	 */
	private BigDecimal refundCouponAmount;

    /**
     * 满赠金额
     */
    private BigDecimal giftAmount;

    /**
     * 满赠退款金额
     */
    private BigDecimal refundGiftAmount;

    /**
     * 秒杀特价金额
     */
    private BigDecimal promotionAmount;

    /**
     * 秒杀特价退款金额
     */
    private BigDecimal refundPromotionAmount;

    /**
     * 组合促销优惠金额
     */
    private BigDecimal comPacAmount;

    /**
     * 退回组合促销优惠的金额
     */
    private BigDecimal refundComPacAmount;

    /**
     * 预售优惠金额
     */
    private BigDecimal presaleDiscountAmount;

    /**
     * 预售优惠退款金额
     */
    private BigDecimal refundPreAmount;

    /**
     * 支付促销优惠金额
     */
    private BigDecimal payDiscountAmount;

    /**
     * 支付促销退款金额
     */
    private BigDecimal refundPayAmount;

    /**
     * 促销单创建时间
     */
    private Date saleCreateTime;

    /**
     * 促销货款单结算时间
     */
    private Date saleSettlementTime;


    /**
     * 预售违约金额
     */
    private BigDecimal presaleDefaultAmount;

    /**
     * 应结算预售违约金金额
     */
    private BigDecimal pdAmount;

    /**
     * 预售违约结算单创建时间
     */
    private Date pdCreateTime;

    /**
     * 预售违约结算时间
     */
    private Date pdSettlementTime;

    /**
	 * 结算总金额
	 */
	private BigDecimal totalAmount;


	public BigDecimal getTotalGoodsAmount() {
		return getAmount(totalGoodsAmount);
	}

	public BigDecimal getRefundGoodsAmount() {
		return getAmount(refundGoodsAmount);
	}

    public BigDecimal getCouponAmount() {
        return getAmount(couponAmount);
    }

    public BigDecimal getRefundCouponAmount() {
        return getAmount(refundCouponAmount);
    }

    public BigDecimal getGiftAmount() {
        return getAmount(giftAmount);
    }

    public BigDecimal getRefundGiftAmount() {
        return getAmount(refundGiftAmount);
    }

    public BigDecimal getPromotionAmount() {
        return getAmount(promotionAmount);
    }

    public BigDecimal getRefundPromotionAmount() {
        return getAmount(refundPromotionAmount);
    }

    public BigDecimal getComPacAmount() {
        return getAmount(comPacAmount);
    }

    public BigDecimal getRefundComPacAmount() {
        return getAmount(refundComPacAmount);
    }

    public BigDecimal getTotalAmount() {
		return getAmount(totalAmount);
	}

    public BigDecimal getPresaleDiscountAmount() {
        return getAmount(presaleDiscountAmount);
    }

    public BigDecimal getRefundPreAmount() {
        return getAmount(refundPreAmount);
    }

    public BigDecimal getPayDiscountAmount() {
        return getAmount(payDiscountAmount);
    }

    public BigDecimal getRefundPayAmount() {
        return getAmount(refundPayAmount);
    }

    public BigDecimal getPresaleDefaultAmount() {
        return getAmount(presaleDefaultAmount);
    }

    public BigDecimal getPdAmount() {
        return getAmount(pdAmount);
    }

    public static BigDecimal getAmount(BigDecimal amount) {
		if (amount == null || BigDecimal.ZERO.compareTo(amount) == 0) {
			return BigDecimal.ZERO;
		} else {
			return amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
	}
}
