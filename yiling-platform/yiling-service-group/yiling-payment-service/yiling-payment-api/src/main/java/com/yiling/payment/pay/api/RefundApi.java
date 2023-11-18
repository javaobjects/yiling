package com.yiling.payment.pay.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.exception.PayException;
import com.yiling.payment.pay.dto.PayOrderDTO;
import com.yiling.payment.pay.dto.request.RefundCallBackRequest;
import com.yiling.payment.pay.dto.request.RefundParamListRequest;
import com.yiling.payment.pay.dto.request.RefundParamRequest;

import javafx.util.Pair;

/**
 * 支付退款
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.api
 * @date: 2021/11/1
 */
public interface RefundApi {

    /**
     * 查询订单可以退款的交易记录(最多可以查询出两条)
     * @param orderPlatform 订单来源
     * @param refundParamRequest
     * @return
     */
    Result<List<PayOrderDTO>> selectCanRefundPayOrder(OrderPlatformEnum orderPlatform,RefundParamRequest refundParamRequest);

    /**
     * 查询订单可以退款的交易记录(最多可以查询出两条)
     * @param refundParamRequest
     * @return
     */
    @Deprecated
    Result<List<PayOrderDTO>> selectCanRefundPayOrder(RefundParamRequest refundParamRequest);

    /**
     * 创建退款
     * @param refundParamRequest
     * @return
     */
    Result<Void> refundPayOrder(RefundParamRequest refundParamRequest);

    /**
     * 创建批量退款接口
     * @param refundParamListRequest
     * @return
     */
    Result<Void> refundPayOrder(RefundParamListRequest refundParamListRequest);

    /**
     * 重推失败退款记录
     * @param refundParamRequest
     * @return
     */
    Result<Void> retryFailurePayOrder(RefundParamRequest refundParamRequest);

    /**
     * 查询超过一天内超时未退款的记录
     * @return
     */
    List<String> selectTimeOutRefundOrderList();

    /**
     * 查询等待退款的退款记录
     * @param limit 查询条数
     * @return 退款单号
     */
    List<String> listWaitRefundList(Integer limit);

    /**
     * 定时触发退款
     * @return
     */
    Result<Void> refundByRefundNo(String  refundNo);

    /**
     * 校验退款中，超时退款，补偿退款
     * @param limit
     * @return
     */
    Result<Void> checkRefundTime(Integer limit);

    /**
     * 退款回调
     * @param refundCallBackRequest
     * @return
     * @throws PayException
     */
    Result<String> operationRefundCallBackThird(RefundCallBackRequest refundCallBackRequest)  throws PayException;;

}
