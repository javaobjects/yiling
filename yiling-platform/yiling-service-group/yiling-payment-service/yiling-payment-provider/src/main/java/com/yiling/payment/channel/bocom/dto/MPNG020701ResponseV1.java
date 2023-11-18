package com.yiling.payment.channel.bocom.dto;

   import com.fasterxml.jackson.annotation.JsonProperty;
   import com.bocom.api.BocomResponse;
   import java.util.List;
   public class MPNG020701ResponseV1 extends BocomResponse {
	/** "响应体"*/
	@JsonProperty("rsp_body")
	private RspBody rspBody;

	public static class RspBody {
     /** 合计已退款金额*/
     @JsonProperty("done_refund_amount")
     private String doneRefundAmount;

     /** 该笔退款对应的交易的订单金额*/
     @JsonProperty("total_amount")
     private String totalAmount;

     /** 本次退款请求对应的退款金额*/
     @JsonProperty("refund_amount")
     private String refundAmount;

     /** 币种目前只支持CNY*/
     @JsonProperty("currency")
     private String currency;

     /** 通道类型
01本行B2C
02跨行B2C
03本行B2B
04跨行B2B
*/
     @JsonProperty("channel_type")
     private String channelType;

     /** 退款单据号*/
     @JsonProperty("refund_order_no")
     private String refundOrderNo;

     /** 交行内部订单号*/
     @JsonProperty("sys_order_no")
     private String sysOrderNo;

     /** 退款支付优惠金额*/
     @JsonProperty("pay_dsct_amount")
     private String payDsctAmount;

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
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getRefundOrderNo() {
		return refundOrderNo;
	}

	public void setRefundOrderNo(String refundOrderNo) {
		this.refundOrderNo = refundOrderNo;
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
}	/** "响应头"*/
	@JsonProperty("rsp_head")
	private RspHead rspHead;

	public static class RspHead {
     /** 交易码*/
     @JsonProperty("trans_code")
     private String transCode;

     /** 响应码*/
     @JsonProperty("response_code")
     private String responseCode;

     /** 交易状态P:处理中 F:失败 S:成功 */
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