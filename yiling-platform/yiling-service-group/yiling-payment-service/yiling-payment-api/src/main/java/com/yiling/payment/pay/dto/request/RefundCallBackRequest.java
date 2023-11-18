package com.yiling.payment.pay.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/** 订单支付回调
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.channel.service.dto
 * @date: 2021/10/20
 */

@Data
@Accessors(chain=true)
@Builder
public class RefundCallBackRequest extends BaseRequest {

    private static final long serialVersionUID=-1135804542218149492L;
    /**
     * 支付方式
     */
    private String payWay;
    /**
     * 支付交易流水号
     */
    private String payNo;

    /**
     * 退款状态
     */
    private Integer refundStatus;

    /**
     * 第三方会写支付状态
     */
    private String thirdState;

    /**
     * 银行参数
     */
    private String bank;

    /**
     * 错误信息
     */
    private String errorMessage;


    /**
     * 支付第三方交易单号
     */
    private String thirdTradeNo;

    /**
     * 第三方退款单号
     */
    private String third_fund_no;

    /**
     * 退款编号
     */
    private String refund_no;

    /**
     * 退款交易日期
     */
    private Date refundDate;

}
