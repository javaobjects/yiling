package com.yiling.settlement.b2b.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

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
public class SettlementOrderDTO extends BaseDTO {

	/**
	 * 订单id
	 */
	private Long orderId;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 卖家企业id
	 */
	private Long sellerEid;

	/**
	 * 买家id
	 */
	private Long buyerEid;

	/**
	 * 促销结算单id
	 */
	private Long saleSettlementId;

	/**
	 * 货款结算单id
	 */
	private Long goodsSettlementId;

    /**
     * 预售违约金结算单id
     */
    private Long pdSettlementId;

	/**
	 * 促销结算单号
	 */
	private String saleSettlementNo;

	/**
	 * 货款结算单号
	 */
	private String goodsSettlementNo;

    /**
     * 预售违约金结算单号
     */
    private String pdSettlementNo;

    /**
     * 结算单类型 1-货款&促销结算单 2-预售违约金结算单
     */
    private Integer settType;

	/**
	 * 总货款金额
	 */
	private BigDecimal totalGoodsAmount;

	/**
	 * 退款的货款金额
	 */
	private BigDecimal refundGoodsAmount;

	/**
	 * 应结算货款金额
	 */
	private BigDecimal goodsAmount;

	/**
	 * 优惠券金额
	 */
	private BigDecimal couponAmount;

	/**
	 * 优惠券退款金额
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
	 * 应结算促销金额
	 */
	private BigDecimal salesAmount;

    /**
     * 预售违约金额
     */
    private BigDecimal presaleDefaultAmount;

    /**
     * 应结算预售违约金金额
     */
    private BigDecimal pdAmount;

	/**
	 * 结算总金额
	 */
	private BigDecimal totalAmount;

	/**
	 * 货款结算状态 1-待结算 2-银行处理中 3-已结算 4-银行处理失败
	 */
	private Integer goodsStatus;

	/**
	 * 促销结算状态 1-待结算 2-银行处理中 3-已结算 4-银行处理失败
	 */
	private Integer saleStatus;

    /**
     * 预售违约结算状态 1-待结算 2-银行处理中 3-已结算 4-银行处理失败
     */
    private Integer presaleDefaultStatus;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 修改人
	 */
	private Long updateUser;

	/**
	 * 备注
	 */
	private String remark;
}
