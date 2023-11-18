package com.yiling.admin.b2b.settlement.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("结算单明细page")
public class SettlementDetailPageVO<T> extends Page<T> {

	/**
	 * 货款金额
	 */
	@ApiModelProperty(value = "货款金额-结算单类型为货款时有效")
	private BigDecimal goodsAmount;

    /**
     * 货款退款金额
     */
    @ApiModelProperty(value = "货款退款金额-结算单类型为货款时有效")
    private BigDecimal refundGoodsAmount;

    /**
     * 优惠总金额
     */
    @ApiModelProperty(value = "优惠总金额-结算单类型为促销时有效")
    private BigDecimal discountAmount;

    /**
     * 优惠总退款金额
     */
    @ApiModelProperty(value = "优惠总退款金额-结算单类型为促销时有效")
    private BigDecimal refundDiscountAmount;

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

    /**
     * 结算金额
     */
    @ApiModelProperty(value = "结算金额-无论结算单类型是什么结算金额都拿该字段")
    private BigDecimal amount;


	/**
	 * 结算单号
	 */
	@ApiModelProperty(value = "结算单号")
	private String code;

	/**
	 * 结算单类型 1-货款结算单 2-促销结算单 3-预售违约金结算单
	 */
	@ApiModelProperty(value = "结算单类型 1-货款结算单 2-促销结算单 3-预售违约金结算单")
	private Integer type;

	/**
	 * 结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败
	 */
	@ApiModelProperty(value = "结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败")
	private Integer status;

	/**
	 * 结算单生成时间
	 */
	@ApiModelProperty(value = "结算单生成时间")
	private Date createTime;

	/**
	 * 支付时间
	 */
	@ApiModelProperty(value = "结算时间")
	private Date settlementTime;

	/**
	 * 结算单备注
	 */
	@ApiModelProperty(value = "结算单备注")
	private String settlementRemark;

	/**
	 * 企业收款账户名称
	 */
	@ApiModelProperty(value = "企业收款账户名称")
	private String name;

	/**
	 * 开户行名称
	 */
	@ApiModelProperty(value = "开户行名称")
	private String bankName;

	/**
	 * 省份name
	 */
	@ApiModelProperty(value = "省份name", hidden = true)
	@JsonIgnore
	private String provinceName;

	/**
	 * 市name
	 */
	@ApiModelProperty(value = "市name", hidden = true)
	@JsonIgnore
	private String cityName;

	/**
	 * 开户地
	 */
	@ApiModelProperty(value = "开户地")
	private String address;

	/**
	 * 支行名称
	 */
	@ApiModelProperty(value = "支行名称")
	private String subBankName;

	/**
	 * 银行账户
	 */
	@ApiModelProperty(value = "银行账户")
	private String account;

	public String getAddress() {
		if (StrUtil.isNotBlank(provinceName)) {
			return provinceName + cityName;

		} else {
			return "-";
		}
	}

	public String getAccount() {
		if (StrUtil.isNotBlank(account)) {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(StrUtil.subPre(account, 5));
			stringBuffer.append("************");
			stringBuffer.append(StrUtil.subSufByLength(account, 3));
			return stringBuffer.toString();
		} else {
			return "-";
		}

	}

	public String getName() {
		if (StrUtil.isNotBlank(name)) {
			return name;
		} else {
			return "-";
		}
	}

	public String getBankName() {
		if (StrUtil.isNotBlank(bankName)) {
			return bankName;
		} else {
			return "-";
		}
	}

	public String getSubBankName() {
		if (StrUtil.isNotBlank(subBankName)) {
			return subBankName;
		} else {
			return "-";
		}
	}

	public BigDecimal getAmount() {
		return amount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getGoodsAmount() {
		return goodsAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

    public BigDecimal getRefundGoodsAmount() {
        return refundGoodsAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getRefundDiscountAmount() {
        return refundDiscountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
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
