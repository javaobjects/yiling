package com.yiling.ih.patient.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 处方订单退款回调 Request
 *
 * @author: fan.shen
 * @date: 2023/5/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HmcPrescriptionOrderRefundNotifyRequest extends BaseRequest {

    /**
     * 订单id
     */
    private Long ihOrderId;

    /**
     * 退款记录id
     */
    private Long refundId;

    /**
     * 退款时间
     */
    private Date refundTime;

    /**
     * 第三方退款单号-微信订单号-交行订单号
     */
    private String thirdPayNo;

    /**
     * 第三方渠道交易流水号
     */
    private String thirdPartyTranNo;

    /**
     * 第三方退款金额
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
