package com.yiling.payment.channel.service;

import java.util.Map;

import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.channel.service.dto.PayOrderResultDTO;
import com.yiling.payment.channel.service.dto.request.ClosePayOrderRequest;
import com.yiling.payment.channel.service.dto.request.CreatePayRequest;
import com.yiling.payment.channel.service.dto.request.CreateRefundRequest;
import com.yiling.payment.channel.service.dto.request.QueryPayOrderRequest;
import com.yiling.payment.pay.dto.RefundOrderResultDTO;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.channel.service
 * @date: 2021/10/15
 */
public interface PayService {

    /**
     * 构建支付代码
     *
     * @param createPayRequest 参阅 {@link CreatePayRequest} <br>
     * @return 付款的数据包
     */
    Result<Map<String, Object>> payData(CreatePayRequest createPayRequest);

    /**
     * 处理用户退款请求.<br>
     *
     * @return 退款数据包
     * @params 参数 {@link  CreateRefundRequest}
     */
    RefundOrderResultDTO refundData(CreateRefundRequest params);

    /**
     * 处理订单查询请求.<br>
     *
     * @param queryPayOrderRequest 查询订单所需要的参数  {@link  QueryPayOrderRequest}
     * @return PayOrderResultDTO 订单数据包
     */
    PayOrderResultDTO orderQuery(QueryPayOrderRequest queryPayOrderRequest);

    /**
     * 处理退款订单查询请求.<br>
     *
     * @param queryPayOrderRequest {@link  QueryPayOrderRequest}
     * @return
     */
    PayOrderResultDTO orderRefundQuery(QueryPayOrderRequest queryPayOrderRequest);

    /**
     * @return
     * @request request 关闭订单交易参数
     */
    Result<Void> closeOrder(ClosePayOrderRequest request);

    /**
     * 获取支付Service
     *
     * @param payWay 支付方式
     * @param paySource 支付来源
     * @return
     */
    boolean matchPayService(String payWay, String paySource);

}
