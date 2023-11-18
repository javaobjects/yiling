package com.yiling.payment.channel.bocom.dto;

   import com.fasterxml.jackson.annotation.JsonProperty;
   import com.bocom.api.BocomResponse;
   import java.util.List;
   public class MPNG210006ResponseV1 extends BocomResponse {
	/** "响应报文体"*/
	@JsonProperty("rsp_body")
	private RspBody rspBody;

	public static class RspBody {
     /** 商户交易编号*/
     @JsonProperty("pay_mer_tran_no")
     private String payMerTranNo;

     /** 商户订单总金额*/
     @JsonProperty("total_amount")
     private String totalAmount;

     /** 第三方活动优惠金额*/
     @JsonProperty("trd_dsct_amount")
     private String trdDsctAmount;

	/** "额外返回的字段"*/
	@JsonProperty("require_values")
	private RequireValues requireValues;

	public static class RequireValues {
     /** 银行端交易流水号*/
     @JsonProperty("bank_tran_no")
     private String bankTranNo;

     /** 微信支付宝详细退货信息*/
     @JsonProperty("refund_info")
     private String refundInfo;

     /** 微信支付宝详细付款信息*/
     @JsonProperty("payment_info")
     private String paymentInfo;

     /** 第三方渠道*/
     @JsonProperty("third_party")
     private String thirdParty;

     /** 第三方渠道交易流水号*/
     @JsonProperty("third_party_tran_no")
     private String thirdPartyTranNo;

	public String getBankTranNo() {
		return bankTranNo;
	}

	public void setBankTranNo(String bankTranNo) {
		this.bankTranNo = bankTranNo;
	}
	public String getRefundInfo() {
		return refundInfo;
	}

	public void setRefundInfo(String refundInfo) {
		this.refundInfo = refundInfo;
	}
	public String getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(String paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	public String getThirdParty() {
		return thirdParty;
	}

	public void setThirdParty(String thirdParty) {
		this.thirdParty = thirdParty;
	}
	public String getThirdPartyTranNo() {
		return thirdPartyTranNo;
	}

	public void setThirdPartyTranNo(String thirdPartyTranNo) {
		this.thirdPartyTranNo = thirdPartyTranNo;
	}
}     /** 币种*/
     @JsonProperty("currency")
     private String currency;

     /** 商品详情*/
     @JsonProperty("detail")
     private String detail;

     /** 买家实付金额*/
     @JsonProperty("buyer_pay_amount")
     private String buyerPayAmount;

     /** 交行内部订单号*/
     @JsonProperty("sys_order_no")
     private String sysOrderNo;

     /** 支付优惠金额*/
     @JsonProperty("pay_dsct_amount")
     private String payDsctAmount;

	public String getPayMerTranNo() {
		return payMerTranNo;
	}

	public void setPayMerTranNo(String payMerTranNo) {
		this.payMerTranNo = payMerTranNo;
	}
	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getTrdDsctAmount() {
		return trdDsctAmount;
	}

	public void setTrdDsctAmount(String trdDsctAmount) {
		this.trdDsctAmount = trdDsctAmount;
	}
	public RequireValues getRequireValues() {
		return requireValues;
	}

	public void setRequireValues(RequireValues requireValues) {
		this.requireValues = requireValues;
	}
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getBuyerPayAmount() {
		return buyerPayAmount;
	}

	public void setBuyerPayAmount(String buyerPayAmount) {
		this.buyerPayAmount = buyerPayAmount;
	}
	public String getSysOrderNo() {
		return sysOrderNo;
	}

	public void setSysOrderNo(String sysOrderNo) {
		this.sysOrderNo = sysOrderNo;
	}
	public String getPayDsctAmount() {
		return payDsctAmount;
	}

	public void setPayDsctAmount(String payDsctAmount) {
		this.payDsctAmount = payDsctAmount;
	}
}	/** "响应报文头"*/
	@JsonProperty("rsp_head")
	private RspHead rspHead;

	public static class RspHead {
     /** 交易标识*/
     @JsonProperty("trans_code")
     private String transCode;

     /** 返回码*/
     @JsonProperty("response_code")
     private String responseCode;

     /** 交易状态 P-处理中  F-失败  S-成功*/
     @JsonProperty("response_status")
     private String responseStatus;

     /** 响应时间*/
     @JsonProperty("response_time")
     private String responseTime;

     /** 返回码描述*/
     @JsonProperty("response_msg")
     private String responseMsg;

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
}	public RspBody getRspBody() {
		return rspBody;
	}

	public void setRspBody(RspBody rspBody) {
		this.rspBody = rspBody;
	}
	public RspHead getRspHead() {
		return rspHead;
	}

	public void setRspHead(RspHead rspHead) {
		this.rspHead = rspHead;
	}
}