package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 市场订单退款回调 Request
 *
 * @author: fan.shen
 * @date: 2022/4/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MarketOrderRefundNotifyRequest extends BaseRequest {

    /**
     * 订单id（必传）
     */
    private Long orderId;

    /**
     * 退款记录id
     */
    private Long refundId;

    /**
     * 退款时间（必传）
     */
    private Date refundTime;

    /**
     * 第三方退款单号（必传）微信订单号-交行订单号
     */
    private String thirdPayNo;

    /**
     * 第三方渠道退款流水号
     */
    private String thirdPartyTranNo;

    /**
     * 第三方退款金额（必传）
     */
    private BigDecimal thirdPayAmount;

    /**
     * 退款状态 0成功 1失败
     */
    private Integer cancelStatus;

    /**
     * 交行内部订单号
     */
    private String sysOrderNo;

}
