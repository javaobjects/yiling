package com.yiling.settlement.b2b.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
 * b2b商家结算单表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b2b_settlement")
public class SettlementDO extends BaseDO {

    private static final long serialVersionUID = 1L;

	/**
	 * 商家eid
	 */
	private Long eid;

	/**
	 * 结算单号
	 */
	private String code;

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
	 * 优惠总金额
	 */
	private BigDecimal discountAmount;

	/**
	 * 优惠总退款金额
	 */
	private BigDecimal refundDiscountAmount;

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
	 * 订单数量
	 */
	private Integer orderCount;

	/**
	 * 收款账户id
	 */
	private Long bankReceiptId;

	/**
	 * 结算单类型 1-货款结算单 2-促销结算单 3-预售违约金结算单
	 */
	private Integer type;

	/**
	 * 结算状态 1-待结算 2-银行处理中 3-已结算 4-银行处理失败 5-发起打款失败
	 */
	private Integer status;

	/**
	 * 支付时间
	 */
	private Date settlementTime;

	/**
	 * 付款通道：1-易宝
	 */
	private Integer paySource;

	/**
	 * 支付通道付款单号
	 */
	private String thirdPayNo;

	/**
	 * 企业付款单号
	 */
	private String payNo;

	/**
	 * 打款失败原因
	 */
	private String errMsg;

	/**
	 * 结算单备注
	 */
	private String settlementRemark;

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


	@TableField(exist = false)
    private List<SettlementDetailDO> settlementDetailDOS;
}
