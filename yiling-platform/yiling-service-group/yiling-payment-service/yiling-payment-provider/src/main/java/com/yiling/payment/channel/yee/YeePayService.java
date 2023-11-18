package com.yiling.payment.channel.yee;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.RefundOrder;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.client.YopRsaClient;
import com.yeepay.g3.sdk.yop.encrypt.DigitalEnvelopeDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.payment.channel.service.dto.PaymentSettlementDTO;
import com.yiling.payment.channel.service.dto.TradeContentJsonDTO;
import com.yiling.payment.channel.service.support.config.YeePayConfig;
import com.yiling.payment.channel.yee.dto.QueryTransferOrderDTO;
import com.yiling.payment.channel.yee.request.YeePayOrder;
import com.yiling.payment.channel.yee.request.YeePayTransferOrder;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.PaySettFeeChargeSideEnum;
import com.yiling.payment.enums.PaySourceEnum;
import com.yiling.payment.enums.PaymentErrorCode;
import com.yiling.payment.enums.TradeStatusEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.enums.WxTypeEnum;
import com.yiling.payment.enums.YeePayEnum;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-11-08
 */
@Component
@Slf4j
public class YeePayService {

    @Autowired
    private YeePayConfig yeePayConfig;

	@Value("${spring.profiles.active}")
	private String env;


	/**
	 * 易宝预下单
	 *
	 * @param order
	 * @param <O>
	 * @return
	 */
	public <O extends YeePayOrder> Map<String, Object> createPreOrder(O order, PaySourceEnum paySourceEnum) {
		if (ObjectUtil.isNull(paySourceEnum)) {
			log.error("易宝支付预下单，paySourceEnum不能为空");
			throw new BusinessException(PaymentErrorCode.YEE_PAY_SOURCE_INVALID);
		}
		//如果是银行卡下单
		if (paySourceEnum == PaySourceEnum.YEE_PAY_BANK) {
			return createOrder(order);
		} else {
			//如果是微信或支付宝下单
			return createAggPayOrder(order, paySourceEnum);
		}
	}

	/**
	 * 易宝支付银行卡预下单
	 *
	 * @param order
	 * @param <O>
	 * @return
	 */
	public <O extends YeePayOrder> Map<String, Object> createOrder(O order) {

        // 获取当前子商户号
        String subMerchant = this.getSubMerchantNo(order);

		Map<String, Object> result = MapUtil.newHashMap();

		YopRequest request = new YopRequest(yeePayConfig.getAppKey(), yeePayConfig.getPrivateKey());
		request.addParam("parentMerchantNo", yeePayConfig.getMerchantNo());
		request.addParam("merchantNo", subMerchant);
		request.addParam("orderId", order.getOutTradeNo());
		request.addParam("orderAmount", order.getPrice());
		request.addParam("notifyUrl",
				new StringBuilder().append(yeePayConfig.getNotifyUrl()).append("?type=").append(YeePayEnum.PAY.getCode()).toString());
        request.addParam("expiredTime", order.getExpirationTime());
        request.addParam("goodsName", order.getBody());
        // 对账备注
        request.addParam("memo", order.getAddition());
        if (StringUtils.isNotBlank(order.getRedirectUrl())) {
            //支付成功后跳转的地址
            request.addParam("redirectUrl", order.getRedirectUrl());
        }

		// 发送请求
		YopResponse response;
		try {
			response = YopRsaClient.post("/rest/v1.0/trade/order", request);
		} catch (Exception exception) {
			log.error("易宝下单失败：{}", exception.getMessage());
			throw new BusinessException(PaymentErrorCode.YEE_CREATE_ORDER_FAIL);
		}

		Map<String, String> responseMap = YeePayUtil.parseResponse(response);
		if (ObjectUtil.notEqual(responseMap.get("code"), YeePayConstants.SUCCESS)) {
			log.error("易宝下单失败，原因:{}", responseMap.get("message"));
			throw new BusinessException(PaymentErrorCode.YEE_CREATE_ORDER_FAIL);
		}
		//返回易宝订单号
		result.put("thirdTradeNo", responseMap.get("uniqueOrderNo"));

		Map<String, String> params = new HashMap<>();
		// 应用
		params.put("appKey", yeePayConfig.getAppKey());
		// 系统商商编
		params.put("merchantNo", yeePayConfig.getMerchantNo());
		// token 调创建订单接口获取
		params.put("token", responseMap.get("token"));
		// 时间戳
		params.put("timestamp", String.valueOf(System.currentTimeMillis()));
//		// 直联编码
//		params.put("directPayType", "WECHAT");
		// 卡类型只适用于银行卡快捷支付
		params.put("cardType", "");
		// 用户标识银行卡快捷支付用于记录绑卡
		params.put("userNo", order.getUserId());
		// 用户标识类型
		params.put("userType", "USER_ID");
		// 扩展字段
        JSONObject ext=new JSONObject();
//        ext.put("reportFee","XIANXIA");
//        params.put("ext", ext.toJSONString());
        params.put("ext", "{\'reportFee\':\'XIANXIA\'}");

		// 需要生成sign签名的参数
		String[] CASHIER = {"appKey", "merchantNo", "token", "timestamp", "directPayType", "cardType", "userNo",
				"userType","ext"};
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < CASHIER.length; i++) {
			String name = CASHIER[i];
			String value = params.get(name);
			if (i != 0) {
				stringBuilder.append("&");
			}
			stringBuilder.append(name + "=").append(value);
		}

		String sign = "";
		try {
			sign = YeePayUtil.getSign(stringBuilder.toString(), yeePayConfig.getAppKey(), yeePayConfig.getPrivateKey());
		} catch (Exception e) {
			log.error("获取易宝签名出现异常，参数：{}，异常原因：{}", stringBuilder.toString(), e);
		}

		String url = "https://cash.yeepay.com/cashier/std";
		url = new StringBuilder().append(url).append("?sign=").append(sign).append("&").append(stringBuilder).toString();
		//标准收银台url
		result.put("standardCashier", url);
        TradeContentJsonDTO contentJsonDTO = new TradeContentJsonDTO();
        contentJsonDTO.setMerchantNo(subMerchant);
        // 返回交易商户号
        result.put("content",JSON.toJSONString(contentJsonDTO));
        result.put("bankOrderId", responseMap.get("bankOrderId"));

        return result;
	}

	/**
	 * 易宝聚合支付预下单
	 *
	 * @param order
	 * @param <O>
	 * @return
	 */
	public <O extends YeePayOrder> Map<String, Object> createAggPayOrder(O order, PaySourceEnum paySourceEnum) {
		Map<String, Object> result = MapUtil.newHashMap();
        // 获取对账子商户号
        String subMerchant = this.getSubMerchantNo(order);

        YopRequest request = new YopRequest(yeePayConfig.getAppKey(), yeePayConfig.getPrivateKey());
        request.addParam("parentMerchantNo", yeePayConfig.getMerchantNo());
        request.addParam("merchantNo", subMerchant);
        request.addParam("orderId", order.getOutTradeNo());
        request.addParam("orderAmount", order.getPrice());
        request.addParam("notifyUrl",
				new StringBuilder().append(yeePayConfig.getNotifyUrl()).append("?type=").append(YeePayEnum.PAY.getCode()).toString());
        request.addParam("expiredTime", order.getExpirationTime());
        request.addParam("goodsName", order.getBody());
        request.addParam("userIp", "36.110.105.58");
        // 对账备注
        request.addParam("memo", order.getAddition());

        //支付宝支付
		if (paySourceEnum == PaySourceEnum.YEE_PAY_ALIPAY) {
			request.addParam("payWay", "USER_SCAN");
			request.addParam("channel", "ALIPAY");
			request.addParam("scene", "OFFLINE");
		}
		//微信支付
		if (paySourceEnum == PaySourceEnum.YEE_PAY_WECHAT) {
			if (ObjectUtil.isEmpty(order.getOpenId())) {
				log.error("易宝聚合支付为微信时openId为空，参数={}", order);
				throw new BusinessException(PaymentErrorCode.YEE_OPEN_ID_INVALID);
			}
            if (ObjectUtil.equal(WxTypeEnum.TypeEnum.gzh,WxTypeEnum.getByAppId(order.getAppId()).getType())) {
                request.addParam("payWay", "WECHAT_OFFIACCOUNT");
            } else if (ObjectUtil.equal(WxTypeEnum.TypeEnum.miniProgram,WxTypeEnum.getByAppId(order.getAppId()).getType())) {
                request.addParam("payWay", "MINI_PROGRAM");
            }

			request.addParam("channel", "WECHAT");
			request.addParam("scene", "OFFLINE");
			request.addParam("userId", order.getOpenId());
			request.addParam("appId", order.getAppId());
		}

		// 发送请求
		YopResponse response;
		try {
			response = YopRsaClient.post("/rest/v1.0/aggpay/pre-pay", request);
		} catch (Exception exception) {
			log.error("易宝聚合支付下单失败：{}", exception);
			throw new BusinessException(PaymentErrorCode.YEE_CREATE_ORDER_FAIL);
		}

		Map<String, String> responseMap = YeePayUtil.parseResponse(response);
        if (ObjectUtil.notEqual(responseMap.get("code"), YeePayConstants.AGG_PAY_SUCCESS)) {
            log.error("易宝聚合支付下单失败，原因:{}", responseMap.get("message"));
            throw new BusinessException(PaymentErrorCode.YEE_CREATE_ORDER_FAIL);
		}
		//返回易宝订单号
		result.put("thirdTradeNo", responseMap.get("uniqueOrderNo"));
		//二维码
		result.put("standardCashier", responseMap.get("prePayTn"));
		// 银行单据Id
        result.put("bankOrderId", responseMap.get("bankOrderId"));

        TradeContentJsonDTO contentJsonDTO = new TradeContentJsonDTO();
        contentJsonDTO.setMerchantNo(subMerchant);

		// 返回交易商户号
        result.put("content",JSON.toJSONString(contentJsonDTO));
		return result;
	}

	/**
	 * 申请退款
	 *
	 * @param refundOrder
	 * @return
	 */
	public Map<String, String> refund(RefundOrder refundOrder) {
        // 退款交易商户号码
        Object merchantNoObj = refundOrder.getAttr("merchantNo");
        // 商户交易号
        String subMerchant = merchantNoObj != null ? merchantNoObj.toString() : yeePayConfig.getMerchantNo();

        YopRequest request = new YopRequest(yeePayConfig.getAppKey(), yeePayConfig.getPrivateKey());
        request.addParam("parentMerchantNo", yeePayConfig.getMerchantNo());
        request.addParam("merchantNo", subMerchant);
        request.addParam("orderId", refundOrder.getOutTradeNo());
        request.addParam("refundRequestId", refundOrder.getRefundNo());
        request.addParam("uniqueOrderNo", refundOrder.getTradeNo());
        request.addParam("refundAmount", String.valueOf(refundOrder.getRefundAmount()));
        request.addParam("description", refundOrder.getDescription());

        // 对账备注
        request.addParam("memo", refundOrder.getAttr("addition"));
        request.addParam("notifyUrl",
				new StringBuilder().append(yeePayConfig.getNotifyUrl()).append("?type=").append(YeePayEnum.REFUND.getCode()).toString());



		// 发送请求
		YopResponse response;
		try {
			log.debug("易宝发起订单退款，参数：{}", JSON.toJSON(request));
			response = YopRsaClient.post("/rest/v1.0/trade/refund", request);
			log.debug("易宝发起订单退款，结果：{}", response.getStringResult());
		} catch (Exception exception) {
			log.error("易宝支付发起退款异常，参数：{}，原因：{}", JSON.toJSON(refundOrder), exception);
			throw new BusinessException(PaymentErrorCode.YEE_REFUND_FAIL);
		}
		return YeePayUtil.parseResponse(response);
	}

	/**
	 * 查询订单
	 *
	 * @param assistOrder
	 * @return
	 */
	public Map<String, String> queryOrder(AssistOrder assistOrder) {
        // 退款交易商户号码
        Object merchantNoObj = assistOrder.getAttr("merchantNo");
        // 商户交易号
        String subMerchant = merchantNoObj != null ? merchantNoObj.toString() : yeePayConfig.getMerchantNo();

        Map<String, String> result = MapUtil.newHashMap();
        YopRequest request = new YopRequest(yeePayConfig.getAppKey(), yeePayConfig.getPrivateKey());
        request.addParam("parentMerchantNo", yeePayConfig.getMerchantNo());
        request.addParam("merchantNo", subMerchant);
        request.addParam("orderId", assistOrder.getOutTradeNo());

		YopResponse yopResponse;
		try {
			yopResponse = YopRsaClient.get("/rest/v1.0/trade/order/query", request);
		} catch (Exception exception) {
			log.error("查询易宝订单失败，参数：{}，原因：{}", JSON.toJSON(assistOrder), exception);
			throw new BusinessException(PaymentErrorCode.YEE_QUERY_ORDER_FAIL);
		}
		if ("FAILURE".equals(yopResponse.getState())) {
			log.error("查询易宝订单失败，参数：{}，原因：{}", JSON.toJSON(assistOrder), yopResponse.getError().getMessage());
			result.put("code", "");
			result.put("message", yopResponse.getError().getMessage());
			return result;
		}

		Map<String, String> responseMap = YeePayUtil.parseResponse(yopResponse);
		String code = responseMap.get("code");
		String message = responseMap.get("message");
		if (ObjectUtil.notEqual(YeePayConstants.SUCCESS, code)) {
			log.error("易宝返回订单查询结果返回失败，参数：{}，原因：{}", JSON.toJSON(assistOrder), message);
		}
		return responseMap;
	}

	/**
	 * 查询退款
	 *
	 * @param refundOrder
	 * @return
	 */
	public Map<String, String> refundQuery(RefundOrder refundOrder) {
        // 退款交易商户号码
        Object merchantNoObj = refundOrder.getAttr("merchantNo");
        // 商户交易号
        String subMerchant = merchantNoObj != null ? merchantNoObj.toString() : yeePayConfig.getMerchantNo();

        Map<String, String> result = MapUtil.newHashMap();
        YopRequest request = new YopRequest(yeePayConfig.getAppKey(), yeePayConfig.getPrivateKey());
        request.addParam("parentMerchantNo", yeePayConfig.getMerchantNo());
        request.addParam("merchantNo", subMerchant);
        request.addParam("orderId", refundOrder.getOutTradeNo());
        request.addParam("refundRequestId", refundOrder.getRefundNo());
        request.addParam("uniqueRefundNo", refundOrder.getTradeNo());

        YopResponse yopResponse;
		try {
			yopResponse = YopRsaClient.get("/rest/v1.0/trade/refund/query", request);
		} catch (Exception exception) {
			log.error("查询易宝退款单失败，参数：{}，原因：{}", JSON.toJSON(refundOrder), exception);
			throw new BusinessException(PaymentErrorCode.YEE_QUERY_REFUND_FAIL);
		}
		if ("FAILURE".equals(yopResponse.getState())) {
			log.error("查询易宝订单失败，参数：{}，原因：{}", JSON.toJSON(refundOrder), yopResponse.getError().getMessage());
			result.put("code", "");
			result.put("message", yopResponse.getError().getMessage());
			return result;
		}

		Map<String, String> responseMap = YeePayUtil.parseResponse(yopResponse);
		String code = responseMap.get("code");
		String message = responseMap.get("message");
		if (ObjectUtil.notEqual(YeePayConstants.SUCCESS, code)) {
			log.error("易宝返回退款单查询结果返回失败，参数：{}，原因：{}", JSON.toJSON(refundOrder), message);
		}
		return responseMap;
	}

	/**
	 * 关闭订单
	 *
	 * @param assistOrder
	 * @return
	 */
	public Map<String, String> close(AssistOrder assistOrder) {
        // 退款交易商户号码
        Object merchantNoObj = assistOrder.getAttr("merchantNo");
        // 商户交易号
        String subMerchant = merchantNoObj != null ? merchantNoObj.toString() : yeePayConfig.getMerchantNo();

		Map<String, String> result = MapUtil.newHashMap();
		YopRequest request = new YopRequest(yeePayConfig.getAppKey(), yeePayConfig.getPrivateKey());
		request.addParam("parentMerchantNo", yeePayConfig.getMerchantNo());
		request.addParam("merchantNo", subMerchant);
		request.addParam("orderId", assistOrder.getOutTradeNo());
		request.addParam("uniqueOrderNo", assistOrder.getTradeNo());
		YopResponse yopResponse;
		try {
			log.debug("易宝发起关闭订单，参数：{}", assistOrder);
			yopResponse = YopRsaClient.post("/rest/v1.0/trade/order/close", request);
			log.debug("易宝发起关闭订单，结果：{}", yopResponse.getStringResult());
		} catch (Exception exception) {
			log.error("易宝关闭订单失败，参数：{}，原因：{}", JSON.toJSON(assistOrder), exception);
			throw new BusinessException(PaymentErrorCode.YEE_QUERY_REFUND_FAIL);
		}
        if ("FAILURE".equals(yopResponse.getState())) {
            log.error("易宝关闭订单失败，参数：{}，原因：{}", JSON.toJSON(assistOrder), yopResponse.getError().getMessage());
            result.put("code", "");
            result.put("message", yopResponse.getError().getMessage());
            return result;
        }
        Map<String, String> responseMap = YeePayUtil.parseResponse(yopResponse);
        String code = responseMap.get("code");
        String message = responseMap.get("message");
        // 表示无需关闭订单，无需打印error日志 无需关闭
        if (ObjectUtil.equal(YeePayConstants.CLOSED_ORDER, code)) {
            log.info("易宝关闭订单失败，参数：{}，原因：{}", JSON.toJSON(assistOrder), message);
            result.put("code", YeePayConstants.SUCCESS);
            result.put("message", message);
            return result;
        }
        if (ObjectUtil.notEqual(YeePayConstants.SUCCESS, code)) {
			log.error("易宝关闭订单失败，参数：{}，原因：{}", JSON.toJSON(assistOrder), message);
		}
		return responseMap;
	}

	/**
	 * 创建向企业付款订单
	 *
	 * @param transferOrder
	 * @return
	 */
	public PaymentSettlementDTO createTransfer(YeePayTransferOrder transferOrder) {
		PaymentSettlementDTO result = new PaymentSettlementDTO();
		result.setPayNo(transferOrder.getOutNo());

		//企业付款
		YopRequest request = new YopRequest(yeePayConfig.getAppKey(), yeePayConfig.getPrivateKey());
		request.addParam("parentMerchantNo", yeePayConfig.getMerchantNo());
		request.addParam("merchantNo", yeePayConfig.getMerchantNo());


		request.addParam("requestNo", transferOrder.getOutNo());
		request.addParam("orderAmount", Constants.DEBUG_ENV_LIST.contains(env) ?
				new BigDecimal("0.01") : transferOrder.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));

		if (ObjectUtil.equal(PaySettFeeChargeSideEnum.USER.getCode(), transferOrder.getFeeChargeSide())) {
			// 手续费承担方(若不传默认付款方) PAYER:付款方 PAYEE:收款方
			request.addParam("feeChargeSide", "PAYEE");
		}
		//到账类型
		String receiveType;
		switch (transferOrder.getReceiveType()) {
			case 1:
				receiveType = "REAL_TIME";
				break;
			case 2:
				receiveType = "TWO_HOUR";
				break;
			case 3:
				receiveType = "NEXT_DAY";
				break;
			default:
				receiveType = "NEXT_DAY";
				break;
		}
		request.addParam("receiveType", receiveType);
		request.addParam("receiverAccountNo", transferOrder.getPayeeAccount());
		request.addParam("receiverAccountName", transferOrder.getPayeeName());
		request.addParam("receiverBankCode", transferOrder.getBankCode());
		// 账户类型可选项如下:DEBIT_CARD:借记卡 CREDIT_CARD:贷记卡
		// QUASI_CREDIT_CARD:准贷卡 PASSBOOK:存折
		// UNIT_SETTLE_CARD:单位结算卡 PUBLIC_CARD:对公卡
//		request.addParam("bankAccountType", "PUBLIC_CARD");
		request.addParam("bankAccountType", Constants.DEBUG_ENV_LIST.contains(env) ? "DEBIT_CARD" : "PUBLIC_CARD");

		request.addParam("comments", transferOrder.getContent());
		request.addParam("notifyUrl", yeePayConfig.getBusinessPayNotifyUrl());

		try {
			YopResponse yopResponse = YopRsaClient.post("/rest/v1.0/account/pay/order", request);
			if ("FAILURE".equals(yopResponse.getState())) {
				log.error("像易宝发起付款失败，原因{}", yopResponse.getError());
				result.setCreateStatus(Boolean.FALSE);

				if (yopResponse.getError() != null) {
					result.setErrMSg(yopResponse.getError().getMessage());
				}
			} else {
				Map<String, String> responseMap = YeePayUtil.parseResponse(yopResponse);
				String returnCode = responseMap.get("returnCode");
				String returnMsg = responseMap.get("returnMsg");
				// 易宝已接受
				if (ObjectUtil.equal(YeePayConstants.BUSINESS_PAY_SUCCESS, returnCode)) {
					result.setCreateStatus(Boolean.TRUE);
					result.setThirdTradeNo(responseMap.get("orderNo"));
				} else {
					// 易宝未接受
					result.setCreateStatus(Boolean.FALSE);
					result.setErrMSg(returnMsg);
				}
			}
			result.setBusinessId(transferOrder.getBusinessId());
			return result;

		} catch (Exception exception) {
			log.error("易宝付款下单失败，原因{}", exception);
			result.setCreateStatus(Boolean.FALSE);
			result.setErrMSg(exception.getMessage());
			result.setBusinessId(transferOrder.getBusinessId());
			return result;
		}
	}

	/**
	 * 查询企业付款状态
	 *
	 * @param payNo
	 * @return
	 */
	public QueryTransferOrderDTO transferQuery(String payNo) {

		QueryTransferOrderDTO result = new QueryTransferOrderDTO();
		result.setPayNo(payNo);

		YopRequest request = new YopRequest(yeePayConfig.getAppKey(), yeePayConfig.getPrivateKey());
		request.addParam("parentMerchantNo", yeePayConfig.getMerchantNo());
		request.addParam("merchantNo", yeePayConfig.getMerchantNo());
		request.addParam("requestNo", payNo);
		try {
			YopResponse yopResponse = YopRsaClient.get("/rest/v1.0/account/pay/query", request);

			if ("FAILURE".equals(yopResponse.getState())) {
				log.error("查询企业付款状态失败，参数{}", payNo);
				if (yopResponse.getError() != null) {
					log.error("查询企业付款状态失败，原因{0}，参数{1}", yopResponse.getError().getMessage(), payNo);
				}
				throw new BusinessException(PaymentErrorCode.YEE_QUERY_TRANSFER_FAIL);
			}

			Map<String, String> responseMap = YeePayUtil.parseResponse(yopResponse);
			String code = responseMap.get("returnCode");
			String msg = responseMap.get("returnMsg");

			//如果易宝返回失败
			if (ObjectUtil.notEqual(YeePayConstants.BUSINESS_PAY_SUCCESS, code)) {
				// 易宝未接受
				result.setTradeStatus(TradeStatusEnum.FALIUE.getCode());
				result.setThirdTradeNo(responseMap.get("orderNo"));
				return result;
			}
			Object status = responseMap.get("status");
			//付款成功
			if (ObjectUtil.equal("SUCCESS", status)) {
				result.setFee(new BigDecimal(responseMap.get("fee")));
				result.setThirdTradeNo(responseMap.get("orderNo"));
				result.setOrderAmount(new BigDecimal(responseMap.get("orderAmount")));
				result.setReceiveAmount(new BigDecimal(responseMap.get("receiveAmount")));
				result.setFinishTime(DateUtil.parseDate(responseMap.get("finishTime")));
				result.setTradeStatus(TradeStatusEnum.SUCCESS.getCode());
			} else if (ObjectUtil.equal("FAIL", status)) {
				//付款失败
				result.setTradeStatus(TradeStatusEnum.FALIUE.getCode());
				result.setThirdTradeNo(responseMap.get("orderNo"));
			} else if (ObjectUtil.equal("CANCELED", status)) {
				//已撤销
				result.setTradeStatus(TradeStatusEnum.CLOSE.getCode());
				result.setThirdTradeNo(responseMap.get("orderNo"));
			} else {
				//冲退标识
				Boolean isReversed = Boolean.parseBoolean(responseMap.get("isReversed"));
				//检查冲退标识
				if (isReversed) {
					result.setTradeStatus(TradeStatusEnum.FALIUE.getCode());
				} else {
					//银行没有充退意味着还在处理
					result.setTradeStatus(TradeStatusEnum.BANK_ING.getCode());
				}
				result.setThirdTradeNo(responseMap.get("orderNo"));
			}
			result.setErrMsg(msg);
			return result;
		} catch (Exception exception) {
			log.error("查询企业付款状态失败，原因{}", exception);
			throw new BusinessException(PaymentErrorCode.YEE_QUERY_TRANSFER_FAIL);
		}
	}

	/**
	 * 解密易宝参数
	 *
	 * @param ciphertext
	 * @return
	 */
	public Map<String, Object> decrypt(String ciphertext) {
		Map<String, Object> map;

		try {
			DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
			dto.setCipherText(ciphertext);
			PrivateKey privateKeyObj = YeePayUtil.getPrivateKey(yeePayConfig.getPrivateKey());
			PublicKey publicKeyObj = YeePayUtil.getPubKey(yeePayConfig.getPublicKey());

			dto = DigitalEnvelopeUtils.decrypt(dto, privateKeyObj, publicKeyObj);
			map = JSON.parseObject(dto.getPlainText(), new TypeReference<TreeMap<String, Object>>() {
			});
		} catch (Exception exception) {
			log.error("易宝回调参数解密失败，参数{}，原因：{}", ciphertext, exception);
			throw new BusinessException(PaymentErrorCode.YEE_DECRYPT_FAIL);
		}
		return map;
	}

	/**
	 * 解密易宝参数=易宝的支付账号
	 *
	 * @param ciphertext
	 * @return
	 */
	public Map<String, Object> decryptForTest(String ciphertext) {
		Map<String, Object> map;

		try {
			DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
			dto.setCipherText(ciphertext);
			PrivateKey privateKeyObj = YeePayUtil.getPrivateKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDHL1ErjAIZ2YJCi4E1MHbPJ2dqcwQF8j2k1LWPKh2t34qFRVgtBC9H9aEvAjBXuJnJhp2W3ij27/Z2bsumaUjh0gnfxQpjaeQhkrsuurU6DSzFpNXHCwutp1NCg4vrsB9qC04CK7EnPg99e4Kp5fCE3IjrRAQ1HVfDFZ9ZtdnuiO21HkvIQtm0Dd3okGJ3ttcQoUjAwqfoSW92q0KTXTFooGyqGijtYhhRm9sCR/+rHneBJ69Rx5a9WBwIf6WeQBNrQTcTh2VEkljxX2kis5V8cQUt8mhEqv2DDJMF6446NG2Gv3j/RWb/K1Zvbi6T56P2by4vEJbML+5bpzjw8jzzAgMBAAECggEABi3IldI9CnI94TSUqIK9XMW7Jok02b5e/SsACtXin88sa9/v0U2s7c2H135442+9jYvYfaKcoLRm79uWA66GbULnvYJORTE2u3fiyJtQHeHkvqjTXN5R2Ww0X/7Wq1QGVXLOU7DpSO9a6aiYMA8K/AQfVzT/8GndnBain9TNUuHsgwQqnH/tHk/yTYsXAu1wppbjWAHUW3AryBFjghruFh5Mm7AJ9CaCDPfO/Q5vuuFKMx25waHQVTs5Ruc53mayvYwEEm1hO41HhiGoQAYAxYknMfLGfHiUhz5AKkkEuTnBVKNX7zy212hruFuXx229Vu1SZWW5isdioGnU+zANCQKBgQDkgpzUCOiBZ4ALlFDGCtXbF/ERSpjSdxfBhRYyZr1X/YB4VW7oMBbV4dmgJ7VF25zs058ZXX7IEhccnUaHXClNqe0VVX7zcianjyngt++mt6QeB+yynMYMUomlGWtzW7n7+mDD6B5xOS4Uni06pABClJcwIAl31ep59I2OzJt2zwKBgQDfJZOFg9mTEyAQ2c10obWYHIIjM4dZH4eoHwTPQxv8Cn5uOaPeXstMEpgNKTiXpVHOn/F+vU98T2Lux/bpavE61QDRudIWyeteWV8qRHP/ctWgHZbX4CxXFTbSJ0hyOneabG0+BVDBoE41gUDvhvH8r7TRxPhlSgTONe7NJVegnQKBgH6mEYvHYC4QkjxYuKf662piC588zBSiIw2D2LiYjsrM2r5XA4A23wEuCvMj7ulH/eKYq65gTltPA3Y9iRCOuOmti5RubU06cEggJeLBr1ako5ZtZ/fawj2kJVvXCeHG7f7FV4pdBTpVqb+Np52/tdExD9aBex3q9uke7LI/ns6rAoGBANnG+mpbe4QmLi1D41foWinR6NAiAf3g36wcWb1NaYpZU3qPlnX3XcEohGyTxj95gJV/U1i1uVVAxPT1AuxbCoK4Tsxf5cGH3sV9w3qkw/CSShqxxf15MszqT05aOEIC/acgGp1k4qOVp75gHtelg5VwhbAT47g2vOnjiON9hj+hAoGAEgcNHsVIbb2AcMINwtRsvxNWMU8/DVEGHcDP0QZg22/UTmKOVwB4ap8sfNgVP+yy0ptwLD0NMe4Aib1AlEsPdGxJuLkwD2YT4nn08gwz3yUTq2p4Ipvj5HvDT3MCcTWiP1LGFPoTir67tsh1c0QGRIxVfdVYDHwP2/3QHfusQbQ=");
			PublicKey publicKeyObj = YeePayUtil.getPubKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6p0XWjscY+gsyqKRhw9MeLsEmhFdBRhT2emOck/F1Omw38ZWhJxh9kDfs5HzFJMrVozgU+SJFDONxs8UB0wMILKRmqfLcfClG9MyCNuJkkfm0HFQv1hRGdOvZPXj3Bckuwa7FrEXBRYUhK7vJ40afumspthmse6bs6mZxNn/mALZ2X07uznOrrc2rk41Y2HftduxZw6T4EmtWuN2x4CZ8gwSyPAW5ZzZJLQ6tZDojBK4GZTAGhnn3bg5bBsBlw2+FLkCQBuDsJVsFPiGh/b6K/+zGTvWyUcu+LUj2MejYQELDO3i2vQXVDk7lVi2/TcUYefvIcssnzsfCfjaorxsuwIDAQAB");

			dto = DigitalEnvelopeUtils.decrypt(dto, privateKeyObj, publicKeyObj);
			map = JSON.parseObject(dto.getPlainText(), new TypeReference<TreeMap<String, Object>>() {
			});
		} catch (Exception exception) {
			log.error("易宝回调参数解密失败，参数{}，原因：{}", ciphertext, exception);
			throw new BusinessException(PaymentErrorCode.YEE_DECRYPT_FAIL);
		}
		return map;
	}

    /**
     *  获取子平台商户号 （为了方便对账，“健康管理中心”或者“会员支付”走单独的商户子账号)
     * @param order
     * @param <O>
     * @return
     */
	private <O extends YeePayOrder> String getSubMerchantNo (O  order) {
        // 交易平台
        OrderPlatformEnum orderPlatformEnum = (OrderPlatformEnum)order.getAttr("orderPlatformEnum");
        // 交易类型
        TradeTypeEnum tradeTypeEnum = (TradeTypeEnum)order.getAttr("tradeTypeEnum");
        // 当前商户号
        String subMerchant = yeePayConfig.getMerchantNo();
        // 如果是hmc订单，特殊处理，走易宝子账号，便于对账需求
        if (OrderPlatformEnum.HMC == orderPlatformEnum) {
            subMerchant =  yeePayConfig.getHmcSubMerchantNo();
            // 如果是会员支付，特殊处理，走易宝子账号，便于对账需求
        } else if (TradeTypeEnum.MEMBER == tradeTypeEnum) {
            subMerchant = yeePayConfig.getMemberMerchantNo();
        }
        return subMerchant;
    }

}
