package com.yiling.payment.pay.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.PayChannelEnum;
import com.yiling.payment.enums.RefundStateEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.exception.PayException;
import com.yiling.payment.pay.dto.PayOrderDTO;
import com.yiling.payment.pay.dto.request.CreatePayOrderRequest;
import com.yiling.payment.pay.dto.request.CreatePayTradeRequest;
import com.yiling.payment.pay.dto.request.InsertTradeLogRequest;
import com.yiling.payment.pay.dto.request.PayCallBackRequest;
import com.yiling.payment.pay.dto.request.RefundParamRequest;

import javafx.util.Pair;

/**
 * 在线支付API
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.api
 * @date: 2021/10/20
 */
public interface PayApi {

    /**
     * 通过payId 查询支付金额
     * @param payId
     * @return
     */
    BigDecimal selectOrderAmountByPayId(String payId);

    /**
     *  通过交易ID查询交易订单
     * @param payId 支付交易ID
     * @return 支付订单基本信息
     * @See PayOrderDTO
     */
    List<PayOrderDTO> listPayOrdersByPayId(String payId);

    /**
     * 创建支付交易订单
     * @param request
     * @return 返回在线支付交易流水ID
     */
    @Deprecated
    Result<String> createPayOrder(CreatePayOrderRequest request);

    /**
     * 创建支付交易订单
     * @param request 创建支付参数
     * @param orderPlatform  对应的平台类型
     * @return 返回在线支付交易流水ID
     */
    Result<String> createPayOrder(OrderPlatformEnum orderPlatform,CreatePayOrderRequest request);

    /**
     *  创建支付交易记录
     * @param createPayTradeRequest
     * @return
     */
    Result<Map<String,Object>> createPayTrade(CreatePayTradeRequest createPayTradeRequest);

    /**
     * @param payNo 支付交易流水号
     * @return
     */
    Result<Boolean> orderQueryByPayNo(String payNo);

    /**
     * 根据支付凭据查询支付记录是否成功
     * @param bankOrderId 银行商户订单号
     * @return
     */
    Pair<Boolean,List<PayOrderDTO>> selectOrderByBankOrderId(String bankOrderId);

    /**
     * 通过订单号查询查询交易记录状态
     * @param tradeType
     * @param orderNo
     * @return
     */
    @Deprecated
    Result<Boolean> orderQueryByOrderNo(TradeTypeEnum tradeType,String orderNo);

    /**
     * 通过订单号查询查询交易记录状态
     * @param tradeType 交易类型
     * @param orderPlatform 订单来源
     * @param orderNo 订单号
     * @return
     */
    Result<Boolean> orderQueryByOrderNo(OrderPlatformEnum orderPlatform,TradeTypeEnum tradeType, String orderNo);

    /**
     * 查询在线支付订单,支付状态，true:支付成功，false:支付失败
     * @param appOrderId 应用订单号
     * @param tradeTypeEnum 在线订单交易类型
     * @return
     */
    Boolean selectAppOrderPayStatus(OrderPlatformEnum orderPlatform,TradeTypeEnum tradeTypeEnum,Long appOrderId);

    /**
     *  第三方支付回调
     * @param  payCallBackRequest 支付回调参数
     * @return
     * @throws PayException
     */
    Result<String> operationCallBackThird(PayCallBackRequest payCallBackRequest) throws PayException;

    /**
     * 添加支付回调日志
     * @param insertTradeLogRequest
     */
    Boolean insertOperationCallBackLog(InsertTradeLogRequest insertTradeLogRequest);

    /**
     * 定时关闭交易超时交易记录
     * @param limit
     * @return
     */
    Result<Void> closeTimer(Integer limit);

    /**
     * 取消支付交易记录
     * @param payId
     * @return
     */
    Result<Void> cancelPayOrder(String payId);

}
