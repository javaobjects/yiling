package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import cn.hutool.core.date.DateUtil;
import lombok.Data;

/**
 * @author dexi.yao
 * @date 2021-11-04
 */
@Data
public class ExportB2bSettlementBO {

	/**
	 * 结算单号
	 */
	private String code;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 支付时间
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
	 * 结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败
	 */
	private Integer status;

	/**
	 * 结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败
	 */
	private String statusStr;

	/**
	 * 结算单备注
	 */
	private String settlementRemark;

	/**
	 * 商家eid
	 */
	private Long eid;

	/**
	 * 商家eid
	 */
	private String ename;

	/**
	 * 货款金额
	 */
	private BigDecimal goodsAmount;

	/**
	 * 货款退款金额
	 */
	private BigDecimal refundGoodsAmount;

    /**
     * 优惠总金额
     */
    private BigDecimal discountAmount;

    /**
     * 优惠总退款金额
     */
    private BigDecimal refundDiscountAmount;

    /**
     * 预售优惠金额
     */
    private BigDecimal presaleDiscountAmount;

    /**
     * 预售优惠退款金额
     */
    private BigDecimal refundPreAmount;

    /**
     * 预售违约金额
     */
    private BigDecimal presaleDefaultAmount;

	/**
	 * 结算金额
	 */
	private BigDecimal amount;

	public Date getSettlementTime() {
		if (settlementTime == null || DateUtil.compare(settlementTime, DateUtil.parseDate("1970-01-01 00:00:00")) <= 0) {
			return null;
		} else {
			return settlementTime;
		}
	}

	public BigDecimal getGoodsAmount() {
		return getAmount(goodsAmount);
	}

	public BigDecimal getRefundGoodsAmount() {
		return getAmount(refundGoodsAmount);
	}

    public BigDecimal getDiscountAmount() {
        return getAmount(discountAmount);
    }

    public BigDecimal getRefundDiscountAmount() {
        return getAmount(refundDiscountAmount);
    }

    public BigDecimal getPresaleDiscountAmount() {
        return getAmount(presaleDiscountAmount);
    }

    public BigDecimal getRefundPreAmount() {
        return getAmount(refundPreAmount);
    }

    public BigDecimal getPresaleDefaultAmount() {
        return getAmount(presaleDefaultAmount);
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
