package com.yiling.b2b.admin.settlement.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

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
public class SettlementDetailPageListItemVO extends BaseVO {

	/**
	 * 订单号
	 */
	@ApiModelProperty(value = "订单号")
	private String orderNo;

	/**
	 * 订单id
	 */
	@ApiModelProperty(value = "订单id")
	private Long orderId;

	/**
	 * 买家企业id
	 */
	@ApiModelProperty(value = "eid",hidden = true)
	@JsonIgnore
	private Long buyerEid;

	/**
	 * 买家企业名称
	 */
	@ApiModelProperty(value = "采购商")
	private String buyerName;

	/**
	 * 结算金额
	 */
	@ApiModelProperty(value = "结算金额")
	private BigDecimal amount;

	/**
	 * 货款金额
	 */
	@ApiModelProperty(value = "货款金额-需根据结算单类型判断与促销金额二取一")
	private BigDecimal goodsAmount;

    /**
     * 货款退款金额
     */
    @ApiModelProperty(value = "货款退款金额-结算单类型为货款时有效")
    private BigDecimal refundGoodsAmount;

    /**
     * 优惠券金额
     */
    @ApiModelProperty(value = "优惠券金额-结算单类型为促销时有效")
    private BigDecimal couponAmount;

    /**
     * 优惠券退款金额
     */
    @ApiModelProperty(value = "优惠券退款金额-结算单类型为促销时有效")
    private BigDecimal refundCouponAmount;

    /**
     * 满赠金额
     */
    @ApiModelProperty(value = "满赠金额-结算单类型为促销时有效")
    private BigDecimal giftAmount;

    /**
     * 满赠退款金额
     */
    @ApiModelProperty(value = "满赠退款金额-结算单类型为促销时有效")
    private BigDecimal refundGiftAmount;

    /**
     * 秒杀特价金额
     */
    @ApiModelProperty(value = "秒杀特价金额-结算单类型为促销时有效")
    private BigDecimal promotionAmount;

    /**
     * 秒杀特价退款金额
     */
    @ApiModelProperty(value = "秒杀特价退款金额-结算单类型为促销时有效")
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
    private BigDecimal refundComPacAmount;

    /**
     * 预售违约金额
     */
    @ApiModelProperty(value = "预售违约金额")
    private BigDecimal presaleDefaultAmount;

    /**
     * 预售违约退款金额
     */
    @ApiModelProperty(value = "预售违约退款金额")
    private BigDecimal refundPresaleDefaultAmount=new BigDecimal("0");

    /**
     * 预售优惠金额
     */
    @ApiModelProperty(value = "预售优惠金额")
    private BigDecimal presaleDiscountAmount;

    /**
     * 预售优惠退款金额
     */
    @ApiModelProperty(value = "预售优惠退款金额")
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


	public BigDecimal getAmount() {
		return amount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getGoodsAmount() {
		return goodsAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

    public BigDecimal getRefundGoodsAmount() {
        return refundGoodsAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCouponAmount() {
        return couponAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getRefundCouponAmount() {
        return refundCouponAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getGiftAmount() {
        return giftAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getRefundGiftAmount() {
        return refundGiftAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPromotionAmount() {
        return promotionAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getRefundPromotionAmount() {
        return refundPromotionAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getComPacAmount() {
        return comPacAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getRefundComPacAmount() {
        return refundComPacAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPresaleDefaultAmount() {
        return presaleDefaultAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPresaleDiscountAmount() {
        return presaleDiscountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getRefundPreAmount() {
        return refundPreAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPayDiscountAmount() {
        return payDiscountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getRefundPayAmount() {
        return refundPayAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
