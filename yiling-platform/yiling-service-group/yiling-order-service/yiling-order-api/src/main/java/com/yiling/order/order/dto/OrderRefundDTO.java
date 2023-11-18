package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 退款表
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderRefundDTO extends BaseDTO {

    /**
     * 退款单号
     */
    private String refundNo;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 采购商名称
     */
    private String buyerEname;

    /**
     * 买家Eid
     */
    private Long buyerEid;

    /**
     * 买家Eid
     */
    private Long sellerEid;

    /**
     * 供应商名称
     */
    private String sellerEname;

    /**
     * 支付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 货款金额
     */
    private BigDecimal totalAmount;

    /**
     * 支付方式
     */
    private String payWay;

    /**
     * 退货单ID
     */
    private Long returnId;

    /**
     * 退货单号
     */
    private String returnNo;

    /**
     * 退款类型1-订单取消退款 2-采购退货退款 3-商家驳回 4-会员订单退款
     */
    private Integer refundType;

    /**
     * 退款来源：1-正常订单，2-会员订单
     */
    private Integer refundSource;

    /**
     * 交易类型(1:付定金,2:支付,3:还款,4:付尾款,5:购买会员)
     */
    private Integer tradeType;

    /**
     * 应退金额
     */
    private BigDecimal refundAmount;

    /**
     * 实际退款金额
     */
    private BigDecimal realRefundAmount;

    /**
     * 退款状态1-待退款 2-退款中 3-退款成功
     */
    private Integer refundStatus;

    /**
     * 支付交易流水号
     */
    private String thirdTradeNo;

    /**
     * 退款交易流水号
     */
    private String thirdFundNo;

    /**
     * 支付内部交易单号
     */
    private String payNo;

    /**
     * 退款时间
     */
    private Date refundTime;

    /**
     * 操作人
     */
    private Long operateUser;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 退款失败原因
     */
    private String failReason;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;



}
