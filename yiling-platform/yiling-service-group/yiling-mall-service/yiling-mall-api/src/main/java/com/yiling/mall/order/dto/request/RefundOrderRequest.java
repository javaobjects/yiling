package com.yiling.mall.order.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.payment.enums.OrderPlatformEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RefundOrderRequest extends BaseRequest {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

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
     * @see com.yiling.order.order.enums.RefundSourceEnum
     */
    private Integer refundSource = 1;

    /**
     * 应退金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 买家Eid
     */
    private Long buyerEid;

    /**
     * 买家Eid
     */
    private Long sellerEid;

    /**
     * 支付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 货款金额
     */
    private BigDecimal totalAmount;
}
