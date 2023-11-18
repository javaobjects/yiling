package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto
 * @date: 2021/10/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class B2BSettlementDTO extends BaseDTO {

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 买家企业ID
     */
    private Long buyerEid;

    /**
     * 买家名称
     */
    private String buyerEname;

    /**
     * 卖家企业ID
     */
    private Long sellerEid;

    /**
     * 卖家名称
     */
    private String sellerEname;

    /**
     * 卖家ERP编码
     */
    private String sellerErpCode;

    /**
     * 配送商企业ID
     */
    private Long distributorEid;

    /**
     * 配送商名称
     */
    private String distributorEname;

    /**
     * 客户内码
     */
    private String customerErpCode;

    /**
     * 商品总金额
     */
    private BigDecimal totalAmount;

    /**
     * 现折金额
     */
    private BigDecimal cashDiscountAmount;

    /**
     * 票折单号
     */
    private String ticketDiscountNo;

    /**
     * 票折金额
     */
    private BigDecimal ticketDiscountAmount;

    /**
     * 平台优惠券金额
     */
    private BigDecimal platformCouponDiscountAmount;

    /**
     * 商家优惠劵金额
     */
    private BigDecimal couponDiscountAmount;

    /**
     * 预售优惠金额
     */
    private BigDecimal presaleDiscountAmount;

    /**
     * 运费
     */
    private BigDecimal freightAmount;

    /**
     * 退货商品金额
     */
    private  BigDecimal returnAmount;

    /**
     * 退货现折金额
     */
    private BigDecimal returnCashDicountAmount;

    /**
     * 退货票折金额
     */
    private BigDecimal returnTicketDiscountAmount;

    /**
     * 退回平台优惠券金额
     */
    private BigDecimal returnPlatformCouponDiscountAmount;

    /**
     * 退回商家优惠劵金额
     */
    private BigDecimal returnCouponDiscountAmount;
    /**
     * 退回预售优惠金额
     */
    private BigDecimal returnPresaleDiscountAmount;

    /**
     * 退回平台支付优惠金额
     */
    private BigDecimal returnPlatformPaymentDiscountAmount;

    /**
     * 退回商家支付优惠金额
     */
    private BigDecimal returnShopPaymentDiscountAmount;

    /**
     * 应付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 支付类型：1-线下支付 2-在线支付
     */
    private Integer paymentType;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    private Integer paymentMethod;

    /**
     * 在线支付渠道
     */
    private String payChannel;

    /**
     * 在线支付方式
     */
    private String paySource;

    /**
     * 支付状态：1-待支付 2-已支付
     */
    private Integer paymentStatus;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 订单类型：1-POP订单
     */
    private Integer orderType;

    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    private Integer orderStatus;

    /**
     * 客户确认状态-20-未转发,-30-待客户确认,-40-已确认
     */
    private Integer customerConfirmStatus;

    /**
     * 订单来源：1-POPPC
     */
    private Integer orderSource;

    /**
     * 收货时间
     */
    private Date receiveTime;

    /**
     * 平台优惠劵平台承担比例
     */
    private BigDecimal platformRatio;
    /**
     * 商家券平台承担比例
     */
    private BigDecimal shopPlatformRatio;

    /**
     * 平台优惠劵商家承担比例
     */
    private BigDecimal platformBusinessRatio;

    /**
     * 商家券商家承担比例
     */
    private BigDecimal shopBusinessRatio;

    /**
     * 满赠活动信息
     */
    private List<OrderPromotionActivityDTO> fullGiftPromotionActivitys;

    /**
     * 下单时间
     */
    private Date createTime;



}
