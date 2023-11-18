package com.yiling.settlement.b2b.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * b2b商家结算单明细表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b2b_settlement_detail")
public class SettlementDetailDO extends BaseDO {

	private static final long serialVersionUID = 1L;

	/**
	 * 结算单id
	 */
	private Long settlementId;

	/**
	 * 订单id
	 */
	private Long orderId;

	/**
	 * 结算单订单数据同步表id
	 */
	private Long orderSyncId;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 买家id
	 */
	private Long buyerEid;

	/**
	 * 卖家企业id
	 */
	private Long sellerEid;

	/**
	 * 结算金额
	 */
	private BigDecimal amount;

	/**
	 * 货款金额
	 */
	private BigDecimal goodsAmount;

	/**
	 * 货款退款金额
	 */
	private BigDecimal refundGoodsAmount;

	/**
	 * 退款金额
	 */
	private BigDecimal refundAmount;

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
	 * 商家券id
	 */
	private Long shopCouponId;

	/**
	 * 平台券id
	 */
	private Long platformCouponId;

	/**
	 * 商家券的平台承担百分比
	 */
	private BigDecimal shopCouponPercent;

	/**
	 * 平台券的平台承担百分比
	 */
	private BigDecimal platformCouponPercent;

	/**
	 * 平台优惠劵金额
	 */
	private BigDecimal platformCouponDiscountAmount;

	/**
	 * 商家优惠券金额
	 */
	private BigDecimal couponDiscountAmount;

	/**
	 * 退回平台优惠券金额
	 */
	private BigDecimal returnPlatformCouponDiscountAmount;

	/**
	 * 退回商家优惠劵金额
	 */
	private BigDecimal returnCouponDiscountAmount;

	/**
	 * 预售违约金额
	 */
	private BigDecimal presaleDefaultAmount;

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
	 * 是否删除：0-否 1-是
	 */
	@TableLogic
	private Integer delFlag;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	private Long createUser;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updateUser;

	/**
	 * 备注
	 */
	private String remark;


}
