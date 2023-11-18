package com.yiling.payment.channel.bocom.dto;

   import com.fasterxml.jackson.annotation.JsonProperty;
   import com.bocom.api.BocomResponse;
   import java.util.List;
   public class MPNG020703ResponseV1 extends BocomResponse {
	/** ""*/
	@JsonProperty("rsp_body")
	private RspBody rspBody;

	public static class RspBody {
     /** 交易状态提示*/
     @JsonProperty("tran_state_msg")
     private String tranStateMsg;

     /** 订单状态*/
     @JsonProperty("order_status")
     private String orderStatus;

     /** 商户内部备注*/
     @JsonProperty("mer_memo")
     private String merMemo;

     /** 合计已退款金额*/
     @JsonProperty("done_refund_amount")
     private String doneRefundAmount;

     /** 该笔退款对应的交易的订单金额*/
     @JsonProperty("total_amount")
     private String totalAmount;

     /** 本次退款请求对应的退款金额*/
     @JsonProperty("refund_amount")
     private String refundAmount;

     /** PROCESS：处理中*/
     @JsonProperty("tran_state")
     private String tranState;

     /** 币种目前只支持CNY*/
     @JsonProperty("currency")
     private String currency;

     /** 交易内容*/
     @JsonProperty("tran_content")
     private String tranContent;

     /** 交易状态码*/
     @JsonProperty("tran_state_code")
     private String tranStateCode;

     /** 退款支付优惠金额*/
     @JsonProperty("pay_dsct_amount")
     private String payDsctAmount;

	public String getTranStateMsg() {
		return tranStateMsg;
	}

	public void setTranStateMsg(String tranStateMsg) {
		this.tranStateMsg = tranStateMsg;
	}
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getMerMemo() {
		return merMemo;
	}

	public void setMerMemo(String merMemo) {
		this.merMemo = merMemo;
	}
	public String getDoneRefundAmount() {
		return doneRefundAmount;
	}

	public void setDoneRefundAmount(String doneRefundAmount) {
		this.doneRefundAmount = doneRefundAmount;
	}
	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getTranState() {
		return tranState;
	}

	public void setTranState(String tranState) {
		this.tranState = tranState;
	}
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getTranContent() {
		return tranContent;
	}

	public void setTranContent(String tranContent) {
		this.tranContent = tranContent;
	}
	public String getTranStateCode() {
		return tranStateCode;
	}

	public void setTranStateCode(String tranStateCode) {
		this.tranStateCode = tranStateCode;
	}
	public String getPayDsctAmount() {
		return payDsctAmount;
	}

	public void setPayDsctAmount(String payDsctAmount) {
		this.payDsctAmount = payDsctAmount;
	}
}	/** ""*/
	@JsonProperty("rsp_head")
	private RspHead rspHead;

	public static class RspHead {
     /** 交易码*/
     @JsonProperty("trans_code")
     private String transCode;

     /** 响应码*/
     @JsonProperty("response_code")
     private String responseCode;

     /** 交易状态*/
     @JsonProperty("response_status")
     private String responseStatus;

     /** 响应时间*/
     @JsonProperty("response_time")
     private String responseTime;

     /** 响应信息*/
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