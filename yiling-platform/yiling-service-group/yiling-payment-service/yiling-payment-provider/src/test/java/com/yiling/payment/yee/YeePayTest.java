package com.yiling.payment.yee;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.RefundOrder;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.client.YopRsaClient;
import com.yiling.payment.BaseTest;
import com.yiling.payment.channel.service.dto.PaymentSettlementDTO;
import com.yiling.payment.channel.yee.YeePayService;
import com.yiling.payment.channel.yee.dto.QueryTransferOrderDTO;
import com.yiling.payment.channel.yee.request.YeePayOrder;
import com.yiling.payment.channel.yee.request.YeePayTransferOrder;
import com.yiling.payment.enums.PaySourceEnum;
import com.yiling.payment.pay.api.YeePayApi;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-11-09
 */
@Slf4j
public class YeePayTest extends BaseTest {

	private static String parentMerchantNo = "10085509459";

	private static String appKey = "app_10085509459";

	private static String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDHL1ErjAIZ2YJCi4E1MHbPJ2dqcwQF8j2k1LWPKh2t34qFRVgtBC9H9aEvAjBXuJnJhp2W3ij27/Z2bsumaUjh0gnfxQpjaeQhkrsuurU6DSzFpNXHCwutp1NCg4vrsB9qC04CK7EnPg99e4Kp5fCE3IjrRAQ1HVfDFZ9ZtdnuiO21HkvIQtm0Dd3okGJ3ttcQoUjAwqfoSW92q0KTXTFooGyqGijtYhhRm9sCR/+rHneBJ69Rx5a9WBwIf6WeQBNrQTcTh2VEkljxX2kis5V8cQUt8mhEqv2DDJMF6446NG2Gv3j/RWb/K1Zvbi6T56P2by4vEJbML+5bpzjw8jzzAgMBAAECggEABi3IldI9CnI94TSUqIK9XMW7Jok02b5e/SsACtXin88sa9/v0U2s7c2H135442+9jYvYfaKcoLRm79uWA66GbULnvYJORTE2u3fiyJtQHeHkvqjTXN5R2Ww0X/7Wq1QGVXLOU7DpSO9a6aiYMA8K/AQfVzT/8GndnBain9TNUuHsgwQqnH/tHk/yTYsXAu1wppbjWAHUW3AryBFjghruFh5Mm7AJ9CaCDPfO/Q5vuuFKMx25waHQVTs5Ruc53mayvYwEEm1hO41HhiGoQAYAxYknMfLGfHiUhz5AKkkEuTnBVKNX7zy212hruFuXx229Vu1SZWW5isdioGnU+zANCQKBgQDkgpzUCOiBZ4ALlFDGCtXbF/ERSpjSdxfBhRYyZr1X/YB4VW7oMBbV4dmgJ7VF25zs058ZXX7IEhccnUaHXClNqe0VVX7zcianjyngt++mt6QeB+yynMYMUomlGWtzW7n7+mDD6B5xOS4Uni06pABClJcwIAl31ep59I2OzJt2zwKBgQDfJZOFg9mTEyAQ2c10obWYHIIjM4dZH4eoHwTPQxv8Cn5uOaPeXstMEpgNKTiXpVHOn/F+vU98T2Lux/bpavE61QDRudIWyeteWV8qRHP/ctWgHZbX4CxXFTbSJ0hyOneabG0+BVDBoE41gUDvhvH8r7TRxPhlSgTONe7NJVegnQKBgH6mEYvHYC4QkjxYuKf662piC588zBSiIw2D2LiYjsrM2r5XA4A23wEuCvMj7ulH/eKYq65gTltPA3Y9iRCOuOmti5RubU06cEggJeLBr1ako5ZtZ/fawj2kJVvXCeHG7f7FV4pdBTpVqb+Np52/tdExD9aBex3q9uke7LI/ns6rAoGBANnG+mpbe4QmLi1D41foWinR6NAiAf3g36wcWb1NaYpZU3qPlnX3XcEohGyTxj95gJV/U1i1uVVAxPT1AuxbCoK4Tsxf5cGH3sV9w3qkw/CSShqxxf15MszqT05aOEIC/acgGp1k4qOVp75gHtelg5VwhbAT47g2vOnjiON9hj+hAoGAEgcNHsVIbb2AcMINwtRsvxNWMU8/DVEGHcDP0QZg22/UTmKOVwB4ap8sfNgVP+yy0ptwLD0NMe4Aib1AlEsPdGxJuLkwD2YT4nn08gwz3yUTq2p4Ipvj5HvDT3MCcTWiP1LGFPoTir67tsh1c0QGRIxVfdVYDHwP2/3QHfusQbQ=";

	@Autowired
	YeePayService  yeePayService;

	@Autowired
	YeePayApi yeePayApi;

	// 初始化client，该Client线程安全，请使用单例模式，多次请求共用
//	TradeClient api = TradeClientBuilder.builder().build();

//	@Test
//	public void test(){
//		YopRequestConfig requestConfig=new YopRequestConfig();
//		requestConfig.setAppKey(appKey);
////		requestConfig.setSecurityReq("YOP-RSA2048-SHA256");
//		OrderRequest request = new OrderRequest();
//		request.withRequestConfig(requestConfig);
//		request.setParentMerchantNo(parentMerchantNo);
//		request.setMerchantNo(parentMerchantNo);
//		request.setOrderId("orderId_example");
//		request.setOrderAmount("100.50");
//		request.setGoodsName("goodsName_example");
//		request.setNotifyUrl("http://www.baidu.com/wui/");
//		request.setMemo("memo_example");
//		request.setExpiredTime("2021-12-12 00:00:00");
//		request.setRedirectUrl("http://oa.yiling.cn/wui/index.html");
//		try {
//			OrderResponse response = api.order(request);
//			System.err.println(response.getResult());
//		} catch (YopClientException e) {
//			System.err.println("Exception when calling TradeClient#order");
//			e.printStackTrace();
//		}
//	}

//	@Test
//	public void test() throws IOException {
//		YopRequest request = new YopRequest(appKey, privateKey);
//		request.addParam("parentMerchantNo", parentMerchantNo);
//		request.addParam("merchantNo", parentMerchantNo);
//		request.addParam("orderId", "1111112");
//		request.addParam("orderAmount", String.valueOf(200));
//		request.addParam("redirectUrl", "http://oa.yiling.cn/wui/index.html");
//		request.addParam("notifyUrl",
//				new StringBuilder().append("http://oa.yiling.cn/wui/index.html").toString());
//		request.addParam("goodsName", "药品等");
//
//		YopResponse response = YopRsaClient.post("/rest/v1.0/trade/order", request);// 发送请求
//
//		Map<String, String> result = YeePayUtil.parseResponse(response.getStringResult());
//		result.get("token");
//
//		Map<String, String> params = new HashMap<>();
//		// 应用
//		params.put("appKey", appKey);
//		// 系统商商编
//		params.put("merchantNo", parentMerchantNo);
//		// token 调创建订单接口获取
//		params.put("token", result.get("token"));
//		// 时间戳
//		params.put("timestamp", String.valueOf(System.currentTimeMillis()));
//		// 直联编码
//		params.put("directPayType", "WECHAT");
//		// 卡类型只适用于银行卡快捷支付
//		params.put("cardType", "");
//		// 用户标识银行卡快捷支付用于记录绑卡
//		params.put("userNo","1");
//		// 用户标识类型
//		params.put("userType", "USER_ID");
//		// 扩展字段
//		params.put("extParamMap", "{\"reportFee\":\"XIANXIA\"}");
//
//		// 需要生成sign签名的参数
//		String[] CASHIER = { "appKey", "merchantNo", "token", "timestamp", "directPayType", "cardType", "userNo",
//				"userType" };
//		StringBuilder stringBuilder = new StringBuilder();
//		for (int i = 0; i < CASHIER.length; i++) {
//			String name = CASHIER[i];
//			String value = params.get(name);
//			if (i != 0) {
//				stringBuilder.append("&");
//			}
//			stringBuilder.append(name + "=").append(value);
//		}
//
//		String sign = "";
//		try {
//			sign = YeePayUtil.getSign(stringBuilder.toString(), appKey, privateKey);
//		} catch (Exception e) {
//			log.error("[YibaoService][toPay]YibaoConfigFactory.getSign出现异常", e);
//		}
//
//		String url = "https://cash.yeepay.com/cashier/std";
////		url = url + ("?sign=" + sign + "&" + stringBuilder);
//		url = new StringBuilder().append(url).append("?sign=").append(sign).append("&").append(stringBuilder).toString();
//		System.err.println(url);
//	}

//	@Test
//	public void test2() throws IOException {
//		YopRequest request = new YopRequest(appKey, privateKey);
//		request.addParam("parentMerchantNo", parentMerchantNo);
//		request.addParam("merchantNo", parentMerchantNo);
//		request.addParam("orderId", "111113");
//		request.addParam("orderAmount", String.valueOf(1));
//		request.addParam("redirectUrl", "http://oa.yiling.cn/wui/index.html");
//		request.addParam("notifyUrl",
//				new StringBuilder().append("http://oa.yiling.cn/wui/index.html").toString());
//		request.addParam("goodsName", "药品等");
//		YopResponse response = YopRsaClient.post("/rest/v1.0/trade/order", request);// 发送请求
//		Map<String, String> result = YeePayUtil.parseResponse(response.getStringResult());
//
//		YopRequest request2 = new YopRequest(appKey, privateKey);
//		request2.addParam("payTool", "SCCANPAY");
//		request2.addParam("payType", "ALIPAY");
//		request2.addParam("token", result.get("token"));
//		request2.addParam("version", "1.0");
//		request2.addParam("extParamMap", "{\"reportFee\":\"XIANXIA\"}");
//		request2.addParam("userIp", "36.110.105.58");
//		YopResponse response2 = YopRsaClient.post("/rest/v1.0/nccashierapi/api/pay", request2);// 发送请求
//		Map<String, String> result2 = YeePayUtil.parseResponse(response2.getStringResult());
//		String resultData = result2.get("resultData");
//		System.err.println(resultData);
//	}

//	@Test
//	public void test3() throws IOException {
//		AssistOrder assistOrder = new AssistOrder();
//		assistOrder.setOutTradeNo("111112");
//		assistOrder.setTradeNo("");
//		Map<String, String> stringStringMap = yeePayService.queryOrder(assistOrder);
//		System.err.println(stringStringMap);
//	}

//	@Test
//	public void test4()  {
//		YeePayOrder payOrder=new YeePayOrder("药品", "药品采购", new BigDecimal("0.1"), "000001", 92525L);
//		Date paymentTimes = DateUtil.offsetHour(new Date(), 2);
//		payOrder.setExpirationTime(paymentTimes);
//		Map<String, Object>  stringStringMap = yeePayService.createOrder(payOrder);
//		System.err.println(stringStringMap);
//	}
//	@Test
//	public void test4()  {
//		YeePayOrder payOrder=new YeePayOrder("药品", "药品采购", new BigDecimal("0.01"), "000002", 92525L,"36.110.105.58");
//		Date paymentTimes = DateUtil.offsetHour(new Date(), 2);
//		payOrder.setExpirationTime(paymentTimes);
//		Map<String, Object>  stringStringMap = yeePayService.createOrder(payOrder);
//		System.err.println(stringStringMap);
//	}
	@Test
	public void test41()  {
		YeePayOrder payOrder=new YeePayOrder("药品", "药品采购", new BigDecimal("0.01"), "00000216", 92525L,"36.110.105.58");
		Date paymentTimes = DateUtil.offsetHour(new Date(), 2);
		payOrder.setExpirationTime(paymentTimes);
		Map<String, Object>  stringStringMap = yeePayService.createPreOrder(payOrder, PaySourceEnum.getBySource("yeePayAlipay"));

		System.err.println(stringStringMap);
	}
	@Test
	public void test42()  {
		YeePayOrder payOrder=new YeePayOrder("药品", "药品采购", new BigDecimal("0.01"), "0000022891112", 92525L,"36.110.105.58");
		Date paymentTimes = DateUtil.offsetHour(new Date(), 2);
		payOrder.setExpirationTime(paymentTimes);
		payOrder.setOpenId("ozvS85G6DplZIKUjdqjxGIJfRkxE");
		Map<String, Object>  stringStringMap = yeePayService.createPreOrder(payOrder, PaySourceEnum.getBySource("yeePayBank"));

		System.err.println(stringStringMap);
	}

	@Test
	public void test5()  {
		String s="x8569rOzi-c9xA0EwjcF_NHowqcsSrUhAAZv0UJOHQf2DfvfuSYqyAFe9wu2hEIT77nDL9EFUUvz3ZRjxtGTmyyZ4pi1Zxxq17Z7GvROtqg4P1hfhfa5UQ4VPPgrW9eRlgGM3w0VI7y6J302Q3ECNDL5mLp3KOH3237P-4pQBaQMyIVMOmajzdLWErh8amhguFU7SOQ-5lMYeOrobV5SMO0lafRCzdMFloVzYlZyErjRjo3uaLRDrz6X9i3pGA21sPuImNvP7TGqAR3lZ0WNmwu-_2KmXWYV1hQpQz7quK5LCUL6KRsQUqSZqw6hmh2OE0nH_akJmmKvFLWgQCzvsQ$xc6B-aaUI1EJNjAIkT0YReovZKVLWepgdLL1eoyQlHXaLpl0sf9xeEAyP0jGNjzwotsxqnPoTginkLAQip8X0Id9ACg-_RNvofdLVw41ULFh2AD-wOTa77BUGau0fTqBzEQLgow_M-DSG9zKOmTl2YzLrRmwFhmmHOx1jXsGAP89ZJ6xEtP-FCRGk5_hZTaqN0OI69mSZJR7h0qafN73_odbNIF7HN1nzntN8ujCQ7Ci-h48ts1XWYUPeLggYIJl5pcyevtK1o_3TnUYZAqdS0JHCy-DV73mH0C0oBL2UzChKxveLbdnOSVJatUMBvTZoip6eBnJ5dzPxwS-Q0e66cmFSATyMZ65bMU2KuGFZ1105BzIhjq8sNjS9QoyIl5JiygO-iS8pofpKG6i4Xq0Y7_HktsRO05e8PehomdnA8e99MnCasfjKmewzt4ETsoZfciER2HK9AOUsM3rxs9F9Mb3R19ODeDtslN-J8HH1JU9CO0t3Ep1sqP3gqLgB3eWlk5KDCJ6dDhpnmDc3ZEagIvklpZzUQ_4szRWrwT-w26wdVHsggCQtQJSorDLEbFfx5I0x72aU5h2tGNSI2lFNExqFVLqVMgy9ECpOPal29JMNfzr2uTRm0sk88C9Nlue0XakI80LoipnoTjfwWLX_AbskPBYXJ2yhHY-13Y1oi4oMRd8vy-vEyy-zadfniHqkdDp3N3Iz0qie7Zx38KnF4SCVp2A6t5OokK4c1EIsD4MDaBFQMGp2MATC6_lcP2bNtWxzI9-VHLaf52MhO9ZDB09IizNcIzZoDcikKLovdWAvt2UlrJ7bjiaKa5myWFQu36kpThKamKHpdVU0nCL5k4FbjuN_i774xrjRzjXlPl71Apkk-whw92IQ4yV6sMY6nHJFQZKNdRajQTug_FBQelvqTs0siyL-rP_dD1ewjufhXaGMJ_oc6w-FJPZvbzGBxLhH9tVkZM7alr8dcxmprwuedkN3IYsZ40zzVybf5Ho6lD6-F2voSsqLkRfRGKT65QANo-DI70Fk21lIexM1ZcpAgNFBSmKPXqU5ee1awCv0_mZVe91C7E4b1d97s9AlqWR-yc8fGxUrc1g9R1a6KZWpmflTSSE069pPfpxQo4yk2sIQlMGEiH-1blP_cVL6BqHIl1IPhV2op_qQS2vpg$AES$SHA256";
		Map<String, Object> decrypt = yeePayApi.decrypt(s);
		System.err.println(decrypt);
	}


	@Test
	public void test6()  {
		RefundOrder refundOrder=new RefundOrder();
		refundOrder.setRefundNo("rf0000003");
		refundOrder.setRefundAmount(new BigDecimal("0.01"));
		refundOrder.setOutTradeNo("000008");
		refundOrder.setTradeNo("1013202111260000002875566803");
		refundOrder.setDescription("测试退款");

		Map<String, String>  stringStringMap = yeePayService.refund(refundOrder);
		System.err.println(stringStringMap);
	}
	@Test
	public void test7()  {
		AssistOrder assistOrder=new AssistOrder();
		assistOrder.setOutTradeNo("000008");

		Map<String, String>  stringStringMap = yeePayService.queryOrder(assistOrder);
		System.err.println(stringStringMap);
	}
	@Test
	public void test8()  {
		RefundOrder refundOrder=new RefundOrder();
		refundOrder.setRefundNo("rf0000001");
		refundOrder.setRefundAmount(new BigDecimal("0.1"));
		refundOrder.setOutTradeNo("000001");
		refundOrder.setTradeNo("1013202111220000002862575087");
		refundOrder.setDescription("测试退款");

		Map<String, String>  stringStringMap = yeePayService.refundQuery(refundOrder);
		System.err.println(stringStringMap);
	}
	@Test
	public void test9()  {
		YeePayTransferOrder yeePayTransferOrder=new YeePayTransferOrder();
		yeePayTransferOrder.setAmount(new BigDecimal("0.01"));
		yeePayTransferOrder.setOutNo("dk00000001");
		yeePayTransferOrder.setReceiveType(1);
		yeePayTransferOrder.setPayeeName("姚德熙");
		yeePayTransferOrder.setPayeeAccount("6214830171972655");
		yeePayTransferOrder.setBankCode("CMBCHINA");
		yeePayTransferOrder.setContent("结算");

		PaymentSettlementDTO stringStringMap = yeePayService.createTransfer(yeePayTransferOrder);
		System.err.println(stringStringMap);
	}

	@Test
	public void test10()  {

		QueryTransferOrderDTO stringStringMap = yeePayService.transferQuery("PT20220328083308279228");
		System.err.println(stringStringMap);
	}

	@Test
	public void test11()  {
		AssistOrder assistOrder=new AssistOrder();
		//payNo
		assistOrder.setOutTradeNo("000002");
		//三方单号
		assistOrder.setTradeNo("1013202112020000002898298172");
		Map<String, String> stringStringMap = yeePayService.close(assistOrder);
		System.err.println(stringStringMap);
	}

//	@Test
//	public void test12()  {
//		WechatConfigAdd0Request request = new WechatConfigAdd0Request();
//		request.setParentMerchantNo("10085509459");
//		request.setMerchantNo("10085509459");
//		request.setTradeAuthDirList("[\"https://cash.yeepay.com/newwap/\",\"https://shouyin.yeepay.com/nc-cashier-wap/\"]");
//		request.setAppIdList("[{\"appId\":\"wx75cd25398f314fed\",\"appSecret\":\"1f448e1202e7aa3ec246cc2d5fa35b36\",\"appIdType\":\"OFFICIAL_ACCOUNT\"}]");
//		try {
//			WechatConfigAdd0Response response = api.wechatConfigAdd_0(request);
//			response = YopRsaClient.post("/rest/v2.0/aggpay/wechat-config/add", request);
//		} catch (IOException ioException) {
//			ioException.printStackTrace();
//		}
//		System.err.println(response.getStringResult());
//	}
	@Test
	public void test12()  {
		Map<String,String> map=new HashMap<>();
		map.put("appId","wx21a99659d9ea1dbd");
		map.put("appSecret","e3d45761968121c0354842ed766829b1");
		map.put("appIdType","MINI_PROGRAM");
		map.put("subscribeAppId","wx21a99659d9ea1dbd");
		ArrayList<Map<String, String>> list = ListUtil.toList(map);
		YopRequest request = new YopRequest(appKey, privateKey);
		request.addParam("parentMerchantNo", "10086207387");
		request.addParam("merchantNo", "10086207387");
		request.addParam("tradeAuthDirList", "[]");
		request.addParam("appIdList", JSON.toJSON(list));
		YopResponse response=null;
		try {
			response = YopRsaClient.post("/rest/v2.0/aggpay/wechat-config/add", request);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		System.err.println(response.getStringResult());
	}

}
