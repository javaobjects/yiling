package com.yiling.payment.channel.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
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
import com.google.common.collect.Lists;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.payment.channel.service.PayService;
import com.yiling.payment.channel.service.TransferService;
import com.yiling.payment.channel.service.dto.PayOrderResultDTO;
import com.yiling.payment.channel.service.dto.PaymentSettlementDTO;
import com.yiling.payment.channel.service.dto.request.ClosePayOrderRequest;
import com.yiling.payment.channel.service.dto.request.CreatePayRequest;
import com.yiling.payment.channel.service.dto.request.CreatePaymentRequest;
import com.yiling.payment.channel.service.dto.request.CreateRefundRequest;
import com.yiling.payment.channel.service.dto.request.QueryPayOrderRequest;
import com.yiling.payment.channel.yee.YeePayConstants;
import com.yiling.payment.channel.yee.YeePayService;
import com.yiling.payment.channel.yee.dto.QueryTransferOrderDTO;
import com.yiling.payment.channel.yee.request.YeePayOrder;
import com.yiling.payment.channel.yee.request.YeePayTransferOrder;
import com.yiling.payment.enums.PayChannelEnum;
import com.yiling.payment.enums.PaySourceEnum;
import com.yiling.payment.enums.RefundStateEnum;
import com.yiling.payment.enums.TradeStatusEnum;
import com.yiling.payment.exception.PayException;
import com.yiling.payment.pay.dto.RefundOrderResultDTO;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-11-08
 */
@Slf4j
@Component("yeePay")
@ConditionalOnBean(YeePayService.class)
public class YeePayServiceImpl implements PayService, TransferService {

	@Autowired
	YeePayService yeePayService;

    @Override
    public List<PaymentSettlementDTO> createPaymentTransfer(List<CreatePaymentRequest> requestList) {
        List<PaymentSettlementDTO> resultList  = Lists.newArrayList();
        for (CreatePaymentRequest request: requestList) {
            YeePayTransferOrder transferOrder = PojoUtils.map(request,YeePayTransferOrder.class);
            transferOrder.setOutNo(request.getPayNo());
            transferOrder.setPayeeAccount(request.getAccount());
            transferOrder.setPayeeName(request.getAccountName());
            log.info("yeepay createPaymentTransfer..request -> " + JSON.toJSONString(transferOrder));
            PaymentSettlementDTO paymentSettlementDTO = yeePayService.createTransfer(transferOrder);
            log.info("yeepay createPaymentTransfer..result -> " + JSON.toJSONString(paymentSettlementDTO));
            paymentSettlementDTO.setTradeStatusEnum(TradeStatusEnum.CLOSE);
            if (paymentSettlementDTO.getCreateStatus()) {
                paymentSettlementDTO.setTradeStatusEnum(TradeStatusEnum.BANK_ING);
            }
            resultList.add(paymentSettlementDTO);
        }
        return resultList;
    }

    @Override
    public QueryTransferOrderDTO transferQuery(String payNo) {
        log.info("yeepay transferQuery..request -> " + JSON.toJSONString(payNo));
        QueryTransferOrderDTO queryTransferOrderDTO = yeePayService.transferQuery(payNo);
        log.info("yeepay transferQuery..result -> " + JSON.toJSONString(queryTransferOrderDTO));
        return queryTransferOrderDTO;
    }

    @Override
	public Result<Map<String, Object>> payData(CreatePayRequest createPayRequest) {
        //App支付
        YeePayOrder payOrder = new YeePayOrder(createPayRequest.getTradeTypeEnum().getSubject(), createPayRequest.getTradeTypeEnum().getBody(), createPayRequest.getAmount(), createPayRequest.getPayNo(), createPayRequest.getUserId(),createPayRequest.getUserIp());
		Date paymentTimes = DateUtil.offsetHour(new Date(),2);
		payOrder.setExpirationTime(paymentTimes);
		payOrder.setOpenId(createPayRequest.getOpenId());
        payOrder.setAppId(createPayRequest.getAppId());
        payOrder.setRedirectUrl(createPayRequest.getRedirectUrl());
        payOrder.setAddition(createPayRequest.getRemark());
        // 交易类型
        payOrder.addAttr("tradeTypeEnum",createPayRequest.getTradeTypeEnum());
        // 订单交易平台
        payOrder.addAttr("orderPlatformEnum",createPayRequest.getOrderPlatformEnum());
		Map<String, Object> map = yeePayService.createPreOrder(payOrder, PaySourceEnum.getBySource(createPayRequest.getPaySource()));
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

            Map<String, String> refundResult = yeePayService.refund(refundOrder);
            log.info("yeepay refundData..request -> " + JSON.toJSONString(refundOrder));
            RefundOrderResultDTO refundOrderResultDTO = new RefundOrderResultDTO();
            refundOrderResultDTO.setRefundId(refundResult.get("uniqueRefundNo"));
            //易宝支付没有errorMessage
            refundOrderResultDTO.setErrorMessage(refundResult.get("message"));
            log.info("yeepay refundData..result -> " + JSON.toJSONString(refundResult));
			//退款状态
			//PROCESSING：处理中
			//SUCCESS：退款成功,首次调用时不会返回,在幂等调用返回该状态
			//FAILED：退款失败
			//CANCEL:退款关闭,商户线下通知易宝结束该笔退款后返回该状态
			String status = refundResult.get("status");
			refundOrderResultDTO.setRefundThirdState(status);
			if ("SUCCESS".equals(status)) {
				refundOrderResultDTO.setRefundStateEnum(RefundStateEnum.SUCCESS);
				return refundOrderResultDTO;
			} else if ("PROCESSING".equals(status)) {
				refundOrderResultDTO.setRefundStateEnum(RefundStateEnum.REFUND_ING);
				return refundOrderResultDTO;
			} else if ("CANCEL".equals(status)) {
				refundOrderResultDTO.setRefundStateEnum(RefundStateEnum.CLOSE);
				return refundOrderResultDTO;
			}else {
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

			if (StringUtils.isNotBlank(queryPayOrderRequest.getMerchantNo())) {
                assistOrder.addAttr("merchantNo", queryPayOrderRequest.getMerchantNo());
            }

            log.info("yeepay.orderQuery...request-> {}",JSON.toJSON(assistOrder));
			Map<String, String> resultMap =  yeePayService.queryOrder(assistOrder);
			String code = resultMap.get("code");
			String msg = resultMap.get("message");
			String paySource = resultMap.get("payWay");
			//PROCESSING：订单待支付
			//SUCCESS：订单支付成功
			//TIME_OUT：订单已过期
			//FAIL:订单支付失败
			//CLOSE:订单关闭
			String tradeStatus = resultMap.get("status");
			String outTradeNo = resultMap.get("orderId");
			String tradeNo = resultMap.get("uniqueOrderNo");
            // 支付成功交易日期
            Object paySuccessDate = resultMap.get("paySuccessDate");
            // 银行订单号,如微信则对应为微信外包交易单号
            String bankOrderId = resultMap.get("bankOrderId");

            //即时失败易宝也仅返回message
			String errorMessage = msg;
			resultDTO.setResultBody(JSONUtil.toJsonStr(resultMap));
			resultDTO.setThirdState(tradeStatus);
			resultDTO.setResultType(1);
			resultDTO.setPayWay(PayChannelEnum.YEEPAY.getCode());
			resultDTO.setPaySource(paySource);
            resultDTO.setOutTradeNo(outTradeNo);
            resultDTO.setTradeDate(Optional.ofNullable(paySuccessDate).map(t -> DateUtil.parse(t.toString(), DatePattern.NORM_DATETIME_FORMAT)).orElse(null));
			log.info("yeepay.orderQuery...result-> {}",resultMap);
			if (YeePayConstants.SUCCESS.equals(code) && ("SUCCESS".equals(tradeStatus))) {
				resultDTO.setTradeNo(tradeNo);
				resultDTO.setErrorMessage(errorMessage);
				resultDTO.setIsSuccess(true);
                resultDTO.setThird_party_tran_no(bankOrderId);
			} else {
				resultDTO.setTradeNo(tradeNo);
				resultDTO.setErrorMessage(errorMessage);
				resultDTO.setIsSuccess(false);
			}
		} catch (Exception e1) {
			log.error("yeePay.orderQuery..{}",e1);
			resultDTO.setErrorMessage("调用易宝查询接口失败!，原因：{"+ e1.getMessage()+"}");
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
			// 易宝交易商户号
            if (StringUtils.isNotBlank(queryPayOrderRequest.getMerchantNo())) {
                refundOrder.addAttr("merchantNo", queryPayOrderRequest.getMerchantNo());
            }
            log.info("yeepay.orderRefundQuery...request:" + JSON.toJSONString(refundOrder));
			Map<String, String> resultMap = yeePayService.refundQuery(refundOrder);
			//PROCESSING：处理中
			//SUCCESS：退款成功
			//FAILED：退款失败
			//CANCEL:退款关闭,商户线下通知易宝结束该笔退款后返回该状态
            String status = resultMap.get("status");
            String tradeNo = resultMap.get("uniqueRefundNo");
            String message = resultMap.get("message");
            // 退款成功交易日期
            Object refundSuccessDate = resultMap.get("refundSuccessDate");

			resultDTO.setResultBody(JSONUtil.toJsonStr(resultMap));
			resultDTO.setThirdState(status);
			resultDTO.setResultType(2);
			resultDTO.setPayWay(PayChannelEnum.YEEPAY.getCode());
            resultDTO.setTradeDate(Optional.ofNullable(refundSuccessDate).map(t -> DateUtil.parse(t.toString(), DatePattern.NORM_DATETIME_FORMAT)).orElse(null));

			if ("SUCCESS".equals(status)) {
				resultDTO.setTradeNo(tradeNo);
				resultDTO.setErrorMessage(message);
				resultDTO.setIsSuccess(true);
			} else {
				resultDTO.setTradeNo(tradeNo);
				resultDTO.setErrorMessage(message);
				resultDTO.setIsSuccess(false);
			}
			log.info("yeepay..orderRefundQuery...result:" + JSON.toJSONString(resultMap));
		} catch (PayErrorException e) {
			resultDTO.setErrorMessage(e.getMessage());
			resultDTO.setIsSuccess(false);

		} catch (Exception e1) {
			log.error("yeePay.orderRefundQuery..{}",e1);
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
        // 易宝交易商户号
        if (StringUtils.isNotBlank(request.getMerchantNo())) {
            assistOrder.addAttr("merchantNo", request.getMerchantNo());
        }
		try {
            log.info("yeepay..closeOrder...request:" + JSON.toJSONString(assistOrder));
			Map<String, String> resultMap =  yeePayService.close(assistOrder);
            log.info("yeepay..closeOrder...result:" + JSON.toJSONString(resultMap));
			String code = resultMap.get("code");
			if (ObjectUtil.equal(YeePayConstants.SUCCESS,code)) {
				return Result.success();
			} else {
				log.error("closeOrder.." + resultMap);
			}
		} catch (PayErrorException e) {
			log.error("关闭订单失败:", e);
		}
		return Result.failed("关闭失败");
	}

	@Override
	public boolean matchPayService(String payWay, String paySource) {
		if (PayChannelEnum.YEEPAY == PayChannelEnum.getByCode(payWay)) {

			return true;
		}
		return false;
	}
}
