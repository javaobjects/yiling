package com.yiling.settlement.b2b.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author dexi.yao
 * @date 2022-04-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b2b_settlement_order_sync")
public class SettlementOrderSyncDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
     * 买家企业id
     */
    private Long buyerEid;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    private Integer paymentMethod;

    /**
     * 支付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 支付退款金额
     */
    private BigDecimal refundPaymentAmount;

    /**
     * 优惠券平台补贴金额=（平台券抵扣金额-退款)*平台券的平台分摊比+（商家券抵扣金额-退款）*商家券的平台分摊比
     */
    private BigDecimal couponAmount;

    /**
     * 优惠券退回平台补贴金额
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
     * 平台券id
     */
    private Long platformCouponId;

    /**
     * 商家券id
     */
    private Long shopCouponId;

    /**
     * 平台券的平台承担百分比
     */
    private BigDecimal platformCouponPercent;

    /**
     * 商家券的平台承担百分比
     */
    private BigDecimal shopCouponPercent;

    /**
     * 平台优惠劵金额
     */
    private BigDecimal platformCouponDiscountAmount;

    /**
     * 商家优惠券金额
     */
    private BigDecimal shopCouponDiscountAmount;

    /**
     * 退回平台优惠券金额
     */
    private BigDecimal returnPlatformCouponDiscountAmount;

    /**
     * 退回商家优惠劵金额
     */
    private BigDecimal returnCouponDiscountAmount;

    /**
     * 预售定金金额
     */
    private BigDecimal presaleDepositAmount;

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
     * 下单时间
     */
    private Date orderCreateTime;

    /**
     * 收货时间
     */
    private Date receiveTime;

    /**
     * 订单取消时间
     */
    private Date cancelTime;

    /**
     * 预售违约状态 1-履约 2-违约
     */
    private Integer defaultStatus;

    /**
     * 订单支付状态：1-待支付 2-已支付
     */
    private Integer paymentStatus;

    /**
     * 订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    private Integer orderStatus;

    /**
     * 结算单生成状态：1-未生成 2-已生成
     */
    private Integer generateStatus;

    /**
     * 货款结算状态(1-未结算,2-已结算)
     */
    private Integer goodsSettlementStatus;

    /**
     * 促销结算状态(1-未结算,2-已结算)
     */
    private Integer saleSettlementStatus;

    /**
     * 预售违约金结算状态(1-未结算,2-已结算)
     */
    private Integer presaleSettlementStatus;

    /**
     * 是否可生成结算单：1-不可生成 2-可生成
     */
    private Integer dataStatus;

    /**
     * 当前订单数据同步状态：1-成功 2-失败
     */
    private Integer status;

    /**
     * 描述
     */
    private String explication;

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
