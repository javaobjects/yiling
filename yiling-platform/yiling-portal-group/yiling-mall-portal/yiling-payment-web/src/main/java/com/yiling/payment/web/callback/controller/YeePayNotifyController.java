package com.yiling.payment.web.callback.controller;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.enums.PayChannelEnum;
import com.yiling.payment.enums.RefundStateEnum;
import com.yiling.payment.enums.TradeStatusEnum;
import com.yiling.payment.enums.YeePayEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.api.PayTransferApi;
import com.yiling.payment.pay.api.RefundApi;
import com.yiling.payment.pay.api.YeePayApi;
import com.yiling.payment.pay.dto.request.InsertTradeLogRequest;
import com.yiling.payment.pay.dto.request.PayCallBackRequest;
import com.yiling.payment.pay.dto.request.RefundCallBackRequest;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-11-09
 */
@RestController
@RequestMapping(value = "/yeePay")
@Api(tags = "易宝支付回调")
@Slf4j
public class YeePayNotifyController extends BaseController {

	@DubboReference
	YeePayApi      yeePayApi;
	@DubboReference
	RefundApi      refundApi;
	@DubboReference
	PayApi         payApi;
    @DubboReference(async = true)
    PayApi         asyncPayApi;
    @DubboReference
	PayTransferApi payTransferApi;


	@RequestMapping(value = "/callBack", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value="易宝支付&退款回调")
	public String callBack(HttpServletRequest request) {
		String response = request.getParameter("response");
		//回调类型 YeePayEnum
		String oprType = request.getParameter("type");
		log.debug("易宝支付开始回调，参数：{}，类型：{}",response,YeePayEnum.getByCode(Integer.valueOf(oprType)).getName());

		if (StrUtil.isBlank(response) || StrUtil.isBlank(oprType)) {
			log.error("易宝回调参数异常，response参数：{}，type参数：{}", response, oprType);
			return "FAIL";
		}
		//参数解密

		Map<String, Object> parMap = yeePayApi.decrypt(response);

		log.debug("易宝回调参数解密，密文：{}，解密参数：{}", response, JSON.toJSON(parMap));
		if (MapUtil.isEmpty(parMap)) {
			log.error("易宝回调参数解密异常，密文：{}，解密参数：{}", response, JSON.toJSON(parMap));
			return "FAIL";
		}
		YeePayEnum type = YeePayEnum.getByCode(Integer.valueOf(oprType));
		//处理回调
		String handleResult = handleCallBack(parMap, type);
		log.debug("订单处理结果，单号：{}，类型：{}，结果：{}",parMap.get("refundRequestId")==null?parMap.get("orderId"):parMap.get("refundRequestId"),
				YeePayEnum.getByCode(Integer.valueOf(oprType)).getName(),handleResult);
		return handleResult;
	}

	@RequestMapping(value = "/paymentCallBack", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value="易宝打款|转账回调")
	public String paymentCallBack(HttpServletRequest request) {
		log.info("易宝付款开始回调，参数：{}",request);
		String response = request.getParameter("response");

		if (StrUtil.isBlank(response)) {
			log.error("易宝之企业付款回调参数异常，response参数：{}", response);
			return "FAIL";
		}
		//参数解密
		Map<String, Object> parMap = yeePayApi.decrypt(response);
		log.info("易宝之企业付款回调参数，密文：{}，解密参数：{}", response, JSON.toJSON(parMap));
		if (MapUtil.isEmpty(parMap)) {
			log.error("易宝之企业付款回调参数异常，密文：{}，解密参数：{}", response, JSON.toJSON(parMap));
			return "FAIL";
		}

		//商户请求号，由商户自定义生成
		String requestNo = parMap.getOrDefault("requestNo", "").toString();
		//易宝支付系统生成的付款订单号
		String orderNo = parMap.getOrDefault("orderNo", "").toString();
		//当付款失败时会返回失败原因，如：银行账户冻结
		String failReason = parMap.getOrDefault("failReason", "").toString();
		//SUCCESS:已到账FAIL:失败（该笔订单付款失败，可重新发起付款）
		String status = parMap.getOrDefault("status", "").toString();
		//手续费
		String fee = parMap.getOrDefault("fee", "0.00").toString();
		//返回收款账户-银行账号
		String receiverAccountNo = parMap.getOrDefault("receiverAccountNo", "").toString();
		//返回收款账户-开户名称
		String receiverAccountName = parMap.getOrDefault("receiverAccountName", "").toString();
		//冲退标识，示例值：true
		Boolean isReversed = Boolean.parseBoolean(parMap.getOrDefault("isReversed", "false").toString());

		//状态
		Integer tradeState = 0;
		//失败原因
		String errorMessage = "";

		//如果打款成功且银行未充退
		if (StrUtil.equals(status, "SUCCESS") && !isReversed) {
			tradeState = TradeStatusEnum.SUCCESS.getCode();
		}
		//该笔订单付款失败，可重新发起付款
		if (StrUtil.equals(status, "FAIL")) {
			tradeState = TradeStatusEnum.FALIUE.getCode();
			errorMessage = failReason;
		}
		//由于银行原因，会发生付款已到账又通知冲退的情况（付款资金会原路退回到商户账户）
		if (isReversed) {
			tradeState = TradeStatusEnum.FALIUE.getCode();
			errorMessage = "银行充退";
		}
        InsertTradeLogRequest tradeLogRequest = InsertTradeLogRequest.builder()
                .payNo(requestNo)
                .tradeType(3)
                .payWay(PayChannelEnum.YEEPAY.getCode())
                .syncLog(JSONUtil.toJsonStr(parMap))
                .build();
        // 添加回调日志
        asyncPayApi.insertOperationCallBackLog(tradeLogRequest);

		//处理支付成功回调
		PayCallBackRequest backRequest = PayCallBackRequest
				.builder()
				.payNo(requestNo)
				.payWay(PayChannelEnum.YEEPAY.getCode())
				.tradeState(tradeState)
				.thirdId(orderNo)
				.thirdState(status)
				.errorMessage(errorMessage)
                .fee(new BigDecimal(fee))
				.bank(receiverAccountNo + "--" + receiverAccountName).build();
		//回调企业打款
		Result<String> stringResult = payTransferApi.transferCallBack(backRequest);
		if (ObjectUtil.equal(stringResult.getCode(), Result.success().getCode())) {
			return "SUCCESS";

		} else {
			return "FAIL";
		}
	}


	/**
	 * 处理易宝下单及退款回调
	 *
	 * @param callBackParMap
	 * @param callBackType
	 * @return
	 */
	public String handleCallBack(Map<String, Object> callBackParMap, YeePayEnum callBackType) {
		// 是否退款回调
		boolean isRefundNotify = ObjectUtil.equal(YeePayEnum.REFUND.getCode(), callBackType.getCode());

		Result<String> result;
		// 表示为退款回调
		if (isRefundNotify) {
			InsertTradeLogRequest tradeLogRequest = InsertTradeLogRequest.builder()
					.payNo(callBackParMap.get("orderId").toString())
					.tradeType(2)
					.payWay(PayChannelEnum.YEEPAY.getCode())
                    .refundNo(callBackParMap.get("refundRequestId").toString())
					.syncLog(JSONUtil.toJsonStr(callBackParMap))
					.build();
			// 添加回调日志
            asyncPayApi.insertOperationCallBackLog(tradeLogRequest);
			//退款状态 SUCCESS：退款成功FAILED:退款失败CANCEL:退款关闭(商户线下通知易宝结束该笔退款后返回该状态)
			String status = callBackParMap.get("status").toString();
            // 退款成功交易日期
            Object refundSuccessDate = callBackParMap.get("refundSuccessDate");

			Integer refundState;
			if ("SUCCESS".equals(status)) {
				refundState = RefundStateEnum.SUCCESS.getCode();
			} else if ("CANCEL".equals(status)) {
				refundState = RefundStateEnum.CLOSE.getCode();
			} else {
				refundState = RefundStateEnum.FALIUE.getCode();
			}
			RefundCallBackRequest request = RefundCallBackRequest.builder()
					.payNo(callBackParMap.get("orderId").toString())
					.payWay(PayChannelEnum.YEEPAY.getCode())
					.thirdTradeNo(callBackParMap.get("uniqueOrderNo").toString())
					.refund_no(callBackParMap.get("refundRequestId").toString())
					.third_fund_no(callBackParMap.get("uniqueRefundNo").toString())
					.thirdState(callBackParMap.get("status").toString())
					.refundStatus(refundState)
					.bank("")
					.errorMessage(callBackParMap.get("errorMessage") != null ? callBackParMap.get("errorMessage").toString() : "")
                    //退款交易日期
                    .refundDate(Optional.ofNullable(refundSuccessDate).map(t -> DateUtil.parse(t.toString(), DatePattern.NORM_DATETIME_FORMAT)).orElse(null))
					.build();

			result = refundApi.operationRefundCallBackThird(request);

		} else {
			InsertTradeLogRequest tradeLogRequest = InsertTradeLogRequest.builder()
					.payNo(callBackParMap.get("orderId").toString())
					.tradeType(1)
					.payWay(PayChannelEnum.YEEPAY.getCode())
					.syncLog(JSONUtil.toJsonStr(callBackParMap))
					.build();
            asyncPayApi.insertOperationCallBackLog(tradeLogRequest);

			//交易状态:SUCCESS（订单支付成功），除成功外易宝文档上没有其他状态
			String trade_status = callBackParMap.get("status").toString();
			Integer tradeState;
			if ("SUCCESS".equals(trade_status)) {
				tradeState = TradeStatusEnum.SUCCESS.getCode();
			} else {
				tradeState = TradeStatusEnum.FALIUE.getCode();
			}
			// 银行账户信息
			Object bankMsg = callBackParMap.get("payerInfo");
			// 支付成功交易日期
			Object paySuccessDate = callBackParMap.get("paySuccessDate");
			// 银行信息
            Object bank = callBackParMap.get("bankOrderId");

			// 使用易宝支付,第三方来源方式
			String paySource = callBackParMap.get("payWay").toString();
			PayCallBackRequest payCallBackRequest = PayCallBackRequest
					.builder().payWay(PayChannelEnum.YEEPAY.getCode())
					.payNo(callBackParMap.get("orderId").toString())
					.thirdId(callBackParMap.get("uniqueOrderNo").toString())
					.thirdState(trade_status)
					.tradeState(tradeState)
                    .thirdPaySource(paySource)
					.bank(bank != null ? bank.toString() : "")
					.errorMessage(callBackParMap.get("errorMessage") != null ? callBackParMap.get("errorMessage").toString() : "")
                    //支付交易日期
                    .tradeDate(Optional.ofNullable(paySuccessDate).map(t -> DateUtil.parse(t.toString(), DatePattern.NORM_DATETIME_FORMAT)).orElse(null))
					.build();
			result = payApi.operationCallBackThird(payCallBackRequest);
		}

		if (HttpStatus.HTTP_OK == result.getCode()) {
			return "SUCCESS";
		} else {
			return "FAIL";
		}
	}
}
