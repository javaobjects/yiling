package com.yiling.order.order.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新订单支付状态
 *
 * @author: xuan.zhou
 * @date: 2021/6/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateOrderPaymentStatusRequest extends BaseRequest {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 订单支付状态
     */
    private Integer paymentStatus;

    /**
     * 支付方式
     */
    private String payWay;

    /**
     * 支付来源:app,pc
     */
    private String paySource;

}
