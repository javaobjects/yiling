package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PageOrderRefundDTO extends BaseDTO {

    /**
     * 退款单号
     */
    private String refundNo;

    /**
     * 退款状态1-待退款 2-退款中 3-退款成功
     */
    private Integer refundStatus;

    /**
     * 退款失败原因
     */
    private String failReason;

    /**
     * 卖家企业ID
     */
    private Long sellerEid;

    /**
     * 卖家名称
     */
    private String sellerEname;

    /**
     * 买家企业ID
     */
    private Long buyerEid;

    /**
     * 买家名称
     */
    private String buyerEname;

    /**
     * 退款类型1-订单取消退款 2-采购退货退款 3-商家驳回 4-会员订单退款
     */
    private Integer refundType;

    /**
     * 退款来源1-正常订单，2-会员订单
     */
    private Integer refundSource;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 退货单号
     */
    private Long returnId;

    /**
     * 退货单号
     */
    private String returnNo;

    /**
     * 支付方式
     */
    private String payWay;

    /**
     * 商品总金额
     */
    private BigDecimal totalAmount;

    /**
     * 应付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 支付交易流水号
     */
    private String thirdTradeNo;

    /**
     * 退款交易流水号
     */
    private String thirdFundNo;

    /**
     * 操作人id
     */
    private Long updateUser;

    /**
     * 修改人
     */
    private String updateUserName;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 退款单创建时间
     */
    private Date createTime;

    /**
     * 退款时间
     */
    private Date refundTime;

    /**
     * 货款退款金额
     */
    private BigDecimal returnAmount;

    /**
     * 平台承担券退款金额
     */
    private BigDecimal platformCouponDiscountAmount;

    /**
     * 商家承担券退款金额
     */
    private BigDecimal couponDiscountAmount;

    /**
     * 操作人
     */
    private Long operateUser;

    /**
     * 操作人姓名
     */
    private String operateUserName;

    /**
     * 操作时间
     */
    private Date operateTime;
}
