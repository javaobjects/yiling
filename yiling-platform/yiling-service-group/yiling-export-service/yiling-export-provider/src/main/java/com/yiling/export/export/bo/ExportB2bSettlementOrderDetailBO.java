package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dexi.yao
 * @date 2021-11-04
 */
@Data
public class ExportB2bSettlementOrderDetailBO {

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
	 * 订单支付时间
	 */
	private Date paymentTime;

	/**
	 * 结算时间
	 */
	private Date settlementTime;

	/**
	 * 结算单类型 1-货款结算单 2-促销结算单 3-预售违约金结算单
	 */
	private Integer type;

	/**
	 * 结算单类型 1-货款结算单 2-促销结算单 3-预售违约金结算单
	 */
	private String typeStr;

	/**
	 * 商品ID
	 */
	private Long goodsId;

//	/**
//	 * 商品ERP内码
//	 */
	/**
	 * 商品内码
	 */
	private String inSn;

	/**
	 * 商品ERP编码
	 */
	private String goodsErpCode;

	/**
	 * 商品名称
	 */
	private String goodsName;

	/**
	 * 商品规格
	 */
	private String goodsSpecification;

	/**
	 * 商品生产厂家
	 */
	private String goodsManufacturer;

	/**
	 * 是否拆包销售：1可拆0不可拆
	 */
	private Integer canSplit;

	/**
	 * 中包装
	 */
	private Integer middlePackage;

	/**
	 * 购买数量
	 */
	private Integer goodsQuantity;

	/**
	 * 发货数量
	 */
	private Integer deliveryQuantity;

	/**
	 * 卖家退货数量
	 */
	private Integer sellerReturnQuantity;

	/**
	 * 收货数量
	 */
	private Integer receiveQuantity;

	/**
	 * 退货数量
	 */
	private Integer returnQuantity;

	/**
	 * 商品单价
	 */
	private BigDecimal goodsPrice;

	/**
	 * 货款金额
	 */
	private BigDecimal goodsAmount;

	/**
	 * 货款退款金额
	 */
	private BigDecimal refundGoodsAmount;

	/**
	 * 货款结算金额
	 */
	private BigDecimal goodsSettleAmount;

	/**
	 * 货款单创建时间
	 */
	private Date goodsCreateTime;

	/**
	 * 货款单结算时间
	 */
	private Date goodsSettlementTime;

    /**
     * 优惠券金额
     */
    private BigDecimal couponAmount;

    /**
     * 优惠券退款金额
     */
    private BigDecimal refundCouponAmount;

    /**
     * 平台承担券结算金额
     */
    private BigDecimal settleCouponAmount;

    /**
     * 秒杀特价金额
     */
    private BigDecimal promotionAmount;

    /**
     * 秒杀特价退款金额
     */
    private BigDecimal refundPromotionAmount;

    /**
     * 秒杀特价结算金额
     */
    private BigDecimal settPromotionAmount;

    /**
     * 组合促销优惠金额
     */
    private BigDecimal comPacAmount;

    /**
     * 退回组合促销优惠的金额
     */
    private BigDecimal refundComPacAmount;

    /**
     * 组合促销结算金额
     */
    private BigDecimal settComPacAmount;

    /**
     * 预售优惠金额
     */
    private BigDecimal presaleDiscountAmount;

    /**
     * 退货商品预售金额
     */
    private BigDecimal returnPresaleDiscountAmount;

    /**
     * 预售促销结算金额
     */
    private BigDecimal settPreSaleAmount;

    /**
     * 平台支付促销优惠金额
     */
    private BigDecimal paymentPlatformDiscountAmount;

    /**
     * 支付促销退款金额
     */
    private BigDecimal refundPayAmount;

    /**
     * 支付促销结算金额
     */
    private BigDecimal settPaySaleAmount;

    /**
     * 预售违约金额
     */
    private BigDecimal presaleDefaultAmount;

    /**
     * 预售违约退款金额
     */
    private BigDecimal refundPresaleDefaultAmount;

    /**
     * 预售违约结算金额
     */
    private BigDecimal preDefSettlementAmount;

	/**
	 * 促销单创建时间
	 */
	private Date saleCreateTime;

	/**
	 * 促销货款单结算时间
	 */
	private Date saleSettlementTime;

    /**
     * 预售结算单创建时间
     */
    private Date pdSettCreateTime;

    /**
     * 预售结算单结算时间
     */
    private Date pdSettlementTime;

	/**
	 * 结算总金额
	 */
	private BigDecimal amount;

	public Date getSettlementTime() {
		if (settlementTime == null || DateUtil.compare(settlementTime, DateUtil.parseDate("1970-01-01 00:00:00")) <= 0) {
			return null;
		} else {
			return settlementTime;
		}
	}

	public BigDecimal getGoodsPrice() {
		return getAmount(goodsPrice);
	}

	public BigDecimal getGoodsAmount() {
		return getAmount(goodsAmount);
	}

	public BigDecimal getRefundGoodsAmount() {
		return getAmount(refundGoodsAmount);
	}

	public BigDecimal getGoodsSettleAmount() {
		return getAmount(goodsSettleAmount);
	}

	public BigDecimal getCouponAmount() {
		return getAmount(couponAmount);
	}

	public BigDecimal getRefundCouponAmount() {
		return getAmount(refundCouponAmount);
	}

	public BigDecimal getSettleCouponAmount() {
		return getAmount(settleCouponAmount);
	}

    public BigDecimal getPromotionAmount() {
        return getAmount(promotionAmount);
    }

    public BigDecimal getRefundPromotionAmount() {
        return getAmount(refundPromotionAmount);
    }

    public BigDecimal getSettPromotionAmount() {
        return getAmount(settPromotionAmount);
    }

    public BigDecimal getComPacAmount() {
        return getAmount(comPacAmount);
    }

    public BigDecimal getRefundComPacAmount() {
        return getAmount(refundComPacAmount);
    }

    public BigDecimal getSettComPacAmount() {
        return getAmount(settComPacAmount);
    }

    public BigDecimal getPresaleDiscountAmount() {
        return getAmount(presaleDiscountAmount);
    }

    public BigDecimal getReturnPresaleDiscountAmount() {
        return getAmount(returnPresaleDiscountAmount);
    }

    public BigDecimal getSettPreSaleAmount() {
        return getAmount(settPreSaleAmount);
    }

    public BigDecimal getPresaleDefaultAmount() {
        return getAmount(presaleDefaultAmount);
    }

    public BigDecimal getRefundPresaleDefaultAmount() {
        return getAmount(refundPresaleDefaultAmount);
    }

    public BigDecimal getPreDefSettlementAmount() {
        return getAmount(preDefSettlementAmount);
    }

    public BigDecimal getAmount() {
		return getAmount(amount);
	}

	public static BigDecimal getAmount(BigDecimal amount) {
		if (amount == null || BigDecimal.ZERO.compareTo(amount) == 0) {
			return BigDecimal.ZERO;
		} else {
			return amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
	}
}
