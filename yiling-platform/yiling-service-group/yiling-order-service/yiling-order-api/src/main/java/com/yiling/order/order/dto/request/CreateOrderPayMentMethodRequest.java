package com.yiling.order.order.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/** 创建订单支付方式
 * @author zhigang.guo
 * @date: 2022/10/12
 */
@Data
@Accessors(chain = true)
public class CreateOrderPayMentMethodRequest extends BaseRequest {

    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 支付类型：1-线下支付 2-在线支付
     */
    private Integer paymentType;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    private Integer paymentMethod;

    /**
     * 交易类型:1-全款 2-定金 3-尾款
     */
    private Integer tradeType;

    /**
     * 在线支付渠道
     */
    private String payChannel;

    /**
     * 在线支付来源
     */
    private String paySource;

    /**
     * 支付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 支付时间
     */
    private Date paymentTime;

}
