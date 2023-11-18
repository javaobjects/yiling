package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.hmc.order.enums.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 市场订单支付回调 Request
 *
 * @author: fan.shen
 * @date: 2022/4/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MarketOrderPayNotifyRequest extends BaseRequest {

    /**
     * 订单id（必传）
     */
    private Long orderId;

    /**
     * 支付时间（必传）
     */
    private Date payTime;

    /**
     * 商户交易编号
     */
    private String merTranNo;

    /**
     * 第三方支付单号（必传）微信订单号-交行订单号
     */
    private String thirdPayNo;

    /**
     * 第三方渠道交易流水号
     */
    private String thirdPartyTranNo;

    /**
     * 第三方支付金额（必传）
     */
    private BigDecimal thirdPayAmount;

}
