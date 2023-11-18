package com.yiling.payment.channel.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.DefaultCurType;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.wx.v3.bean.WxTransactionType;
import com.egzosn.pay.wx.v3.utils.WxConst;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.channel.bocom.BocomPayConstants;
import com.yiling.payment.channel.bocom.BocomPayService;
import com.yiling.payment.channel.bocom.dto.MPNG020701ResponseV1;
import com.yiling.payment.channel.bocom.dto.MPNG020702ResponseV1;
import com.yiling.payment.channel.bocom.dto.MPNG020703ResponseV1;
import com.yiling.payment.channel.bocom.dto.MPNG020705ResponseV1;
import com.yiling.payment.channel.service.PayService;
import com.yiling.payment.channel.service.dto.PayOrderResultDTO;
import com.yiling.payment.channel.service.dto.request.ClosePayOrderRequest;
import com.yiling.payment.channel.service.dto.request.CreatePayRequest;
import com.yiling.payment.channel.service.dto.request.CreateRefundRequest;
import com.yiling.payment.channel.service.dto.request.QueryPayOrderRequest;
import com.yiling.payment.channel.yee.request.YeePayOrder;
import com.yiling.payment.enums.PayChannelEnum;
import com.yiling.payment.enums.PaySourceEnum;
import com.yiling.payment.enums.PaymentErrorCode;
import com.yiling.payment.enums.RefundStateEnum;
import com.yiling.payment.exception.PayException;
import com.yiling.payment.pay.dto.RefundOrderResultDTO;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 交通银行支付
 *
 * @author zhigang.guo
 * @date: 2023/5/8
 */
@Slf4j
@Component("bocomPay")
@ConditionalOnBean(BocomPayService.class)
public class BocomPayServiceImpl implements PayService {
    @Autowired
    private BocomPayService bocomPayService;

    @Override
    public Result<Map<String, Object>> payData(CreatePayRequest createPayRequest) {
        //App支付
        YeePayOrder payOrder = new YeePayOrder(createPayRequest.getTradeTypeEnum().getSubject(), createPayRequest.getTradeTypeEnum().getBody(), createPayRequest.getAmount(), createPayRequest.getPayNo(), createPayRequest.getUserId(), createPayRequest.getUserIp());

        Date paymentTimes = DateUtil.offsetHour(new Date(), 2);
        payOrder.setExpirationTime(paymentTimes);
        payOrder.setOpenId(createPayRequest.getOpenId());
        payOrder.setAppId(createPayRequest.getAppId());
        payOrder.setRedirectUrl(createPayRequest.getRedirectUrl());
        payOrder.setAddition(createPayRequest.getRemark());
        // 交易类型
        payOrder.addAttr("tradeTypeEnum", createPayRequest.getTradeTypeEnum());
        // 订单交易平台
        payOrder.addAttr("orderPlatformEnum", createPayRequest.getOrderPlatformEnum());

        Map<String, Object> map = bocomPayService.createPreOrder(payOrder, PaySourceEnum.getBySource(createPayRequest.getPaySource()));
        // 交易流水号
        map.put("payNo", createPayRequest.getPayNo());
        // 支付失效时间
        map.put("paymentTimes", paymentTimes);
        log.info("唤起支付参数:{}", map);
        // 失效时间
        return Result.success(map);
    }

    @Override
    public RefundOrderResultDTO refundData(CreateRefundRequest params) {
        try {
            RefundOrder refundOrder = new RefundOrder();
            refundOrder.setCurType(DefaultCurType.CNY);
            refundOrder.setTradeNo(params.getThirdTradeNo());
            refundOrder.setOutTradeNo(params.getPayNo());
            refundOrder.setRefundNo(params.getRefundNo());
            refundOrder.setTotalAmount(params.getAmount());
            refundOrder.setRefundAmount(params.getRefundAmount());
            refundOrder.setOrderDate(new Date());
            refundOrder.setDescription(params.getReason());
            refundOrder.addAttr(WxConst.NOTIFY_URL, "");
            refundOrder.addAttr("addition",params.getRemark());
            refundOrder.addAttr("merchantNo",params.getMerchantNo());

            MPNG020701ResponseV1 refundResult = bocomPayService.refund(refundOrder);

            if (!refundResult.isSuccess())  {
                log.error("退款申请失败:", refundResult.getRspMsg());

                throw new PayException("退款申请失败");
            }

            log.info("bocomPay refundData..request -> " + JSON.toJSONString(refundOrder));
            RefundOrderResultDTO refundOrderResultDTO = new RefundOrderResultDTO();
            //易宝支付没有errorMessage
            refundOrderResultDTO.setErrorMessage(refundResult.getRspHead().getResponseMsg());
            log.info("bocomPay refundData..result -> " + JSON.toJSONString(refundResult));
            refundOrderResultDTO.setRefundThirdState(refundResult.getRspHead().getResponseStatus());

            String responseStatus = refundResult.getRspHead().getResponseStatus();

            if (BocomPayConstants.SUCCESS.equals(responseStatus)) {
                refundOrderResultDTO.setRefundId(refundResult.getRspBody().getRefundOrderNo());
                refundOrderResultDTO.setRefundStateEnum(RefundStateEnum.SUCCESS);
                return refundOrderResultDTO;
            } else if (BocomPayConstants.PROCESSING.equals(responseStatus)) {
                refundOrderResultDTO.setRefundId(refundResult.getRspBody().getRefundOrderNo());
                refundOrderResultDTO.setRefundStateEnum(RefundStateEnum.REFUND_ING);
                return refundOrderResultDTO;
            } else {
                refundOrderResultDTO.setRefundStateEnum(RefundStateEnum.FALIUE);
            }
            return refundOrderResultDTO;

        } catch (Exception e) {
            log.error("退款申请失败:", e);

            throw new PayException("退款申请失败");
        }
    }

    @Override
    public PayOrderResultDTO orderQuery(QueryPayOrderRequest queryPayOrderRequest) {
        PayOrderResultDTO resultDTO = new PayOrderResultDTO();
        resultDTO.setOutTradeNo(queryPayOrderRequest.getPayNo());
        try {
            AssistOrder assistOrder = new AssistOrder();
            assistOrder.setOutTradeNo(queryPayOrderRequest.getPayNo());
            assistOrder.setTradeNo(queryPayOrderRequest.getThirdTradeNo());

            log.info("bocomPay.orderQuery...request-> {}", JSON.toJSON(assistOrder));
            MPNG020702ResponseV1 resultMap = bocomPayService.queryOrder(assistOrder);

            if (!resultMap.isSuccess()) {

                log.error("查询订单失败:", resultMap.getRspMsg());

                throw new BusinessException(PaymentErrorCode.BOCOM_QUERY_ORDER_FAIL);
            }

            String status = resultMap.getRspBody().getTranState();
            Boolean isSuccess = BocomPayConstants.QUERYSUCCESS.equals(status);
            resultDTO.setResultBody(JSONUtil.toJsonStr(resultMap));
            resultDTO.setThirdState(resultMap.getRspBody().getTranState());
            resultDTO.setResultType(1);
            resultDTO.setPayWay(PayChannelEnum.BOCOMPAY.getCode());
            //  resultDTO.setPaySource(paySource);
            resultDTO.setOutTradeNo(queryPayOrderRequest.getPayNo());
            resultDTO.setErrorMessage(resultMap.getRspBody().getTranStateMsg());
            resultDTO.setTradeNo(resultMap.getRspBody().getSysOrderNo());
            resultDTO.setIsSuccess(isSuccess);
            // 处理支付成功日期,交行没有返回自己定义一个
            if (isSuccess) {
                resultDTO.setTradeDate(new Date());
            }

            if (resultMap.getRspBody().getRequireValues() != null) {
                resultDTO.setThird_party_tran_no(resultMap.getRspBody().getRequireValues().getThirdPartyTranNo());
            }

            log.info("bocomPay.orderQuery...result-> {}", resultMap);
        } catch (Exception e1) {

            log.error("bocomPay.orderQuery..{}", ExceptionUtil.getMessage(e1));
            resultDTO.setErrorMessage("调用交行查询接口失败!，原因：{" + e1.getMessage() + "}");
            resultDTO.setIsSuccess(false);
        }
        return resultDTO;
    }

    @Override
    public PayOrderResultDTO orderRefundQuery(QueryPayOrderRequest queryPayOrderRequest) {
        PayOrderResultDTO resultDTO = new PayOrderResultDTO();
        resultDTO.setOutTradeNo(queryPayOrderRequest.getPayNo());
        try {
            RefundOrder refundOrder = new RefundOrder();
            refundOrder.setTradeNo(queryPayOrderRequest.getThirdTradeNo());
            refundOrder.setOutTradeNo(queryPayOrderRequest.getPayNo());
            refundOrder.setRefundNo(queryPayOrderRequest.getRefund_no());
            log.info("bocomPay.orderRefundQuery...request:" + JSON.toJSONString(refundOrder));
            MPNG020703ResponseV1 resultMap = bocomPayService.refundQuery(refundOrder);

            if (!resultMap.isSuccess())  {
                log.error("退款查询失败:", resultMap.getRspMsg());
                throw new PayException("退款查询失败");
            }
            //PROCESS：处理中
            //SUCCESS：退款成功
            //FAILURE：退款失败
            String status = resultMap.getRspBody().getTranState();
            String message = resultMap.getRspBody().getTranStateMsg();

            resultDTO.setResultBody(JSONUtil.toJsonStr(resultMap));
            resultDTO.setThirdState(status);
            resultDTO.setResultType(2);
            resultDTO.setPayWay(PayChannelEnum.BOCOMPAY.getCode());
            resultDTO.setTradeDate(new Date());

            if (BocomPayConstants.QUERYSUCCESS.equals(status)) {
                resultDTO.setTradeNo(queryPayOrderRequest.getRefund_no());
                resultDTO.setErrorMessage(message);
                resultDTO.setIsSuccess(true);
            } else {
                resultDTO.setTradeNo(queryPayOrderRequest.getRefund_no());
                resultDTO.setErrorMessage(message);
                resultDTO.setIsSuccess(false);
            }
            log.info("bocomPay..orderRefundQuery...result:" + JSON.toJSONString(resultMap));
        } catch (PayErrorException e) {
            resultDTO.setErrorMessage(e.getMessage());
            resultDTO.setIsSuccess(false);

        } catch (Exception e1) {
            log.error("bocomPay.orderRefundQuery..{}",e1);
            resultDTO.setErrorMessage("调用易宝查询接口失败!");
            resultDTO.setIsSuccess(false);
        }
        return resultDTO;
    }

    @Override
    public Result<Void> closeOrder(ClosePayOrderRequest request) {

        AssistOrder assistOrder = new AssistOrder();
        assistOrder.setOutTradeNo(request.getPayNo());
        assistOrder.setTradeNo(request.getThirdTradeNo());
        assistOrder.setTransactionType(WxTransactionType.APP);

        try {
            log.info("bocomPay..closeOrder...request:" + JSON.toJSONString(assistOrder));
            MPNG020705ResponseV1 resultMap = bocomPayService.close(assistOrder);
            log.info("bocomPay..closeOrder...result:" + JSON.toJSONString(resultMap));

            if (!resultMap.isSuccess()) {
                log.error("关闭订单失败:", resultMap.getRspMsg());
                throw new BusinessException(PaymentErrorCode.BOCOM_CLOSE_ORDER_FAIL);
            }
            String responseStatus = resultMap.getRspHead().getResponseStatus();

            if (!BocomPayConstants.FAILED.equals(responseStatus)) {
               return Result.success();
            } else {
                log.warn("关闭订单异常:", resultMap.getRspHead().getResponseMsg());
            }

        } catch (PayErrorException e) {
            log.error("关闭订单失败:", e);
        }
        return Result.failed("关闭失败");
    }

    @Override
    public boolean matchPayService(String payWay, String paySource) {

        if (PayChannelEnum.BOCOMPAY == PayChannelEnum.getByCode(payWay)) {

            return true;
        }

        return false;
    }
}
