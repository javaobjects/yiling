package com.yiling.payment.channel.bocom.request;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yiling.payment.channel.bocom.dto.MPNG020706ResponseV1;


public class MPNG020706RequestV1 extends AbstractBocomRequest<MPNG020706ResponseV1> {

  @Override
  public Class<MPNG020706ResponseV1> getResponseClass() {
    return MPNG020706ResponseV1.class;
  }

  @Override
  public boolean isNeedEncrypt() {
    return false;
  }

  @Override
  public String getMethod() {
    return "POST";
  }

  @Override
  public Class<? extends BizContent> getBizContentClass() {
    return MPNG020706RequestV1Biz.class;
  }

  public static class MPNG020706RequestV1Biz implements BizContent {

	/** "req_head"*/
	@JsonProperty("req_head")
	private ReqHead reqHead;

	public static class ReqHead {
     /** 交易时间 yyyymmddhhmmss*/
     @JsonProperty("trans_time")
     private String transTime;

     /** 终端版本信息*/
     @JsonProperty("version")
     private String version;

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}	/** "req_body"*/
	@JsonProperty("req_body")
	private ReqBody reqBody;

	public static class ReqBody {
     /** 商户交易编号*/
     @JsonProperty("cancel_mer_tran_no")
     private String cancelMerTranNo;

     /** 商户侧原交易日期*/
     @JsonProperty("mer_trade_date")
     private String merTradeDate;

     /** 服务商编号*/
     @JsonProperty("partner_id")
     private String partnerId;

     /** 商户支付交易编号*/
     @JsonProperty("pay_mer_tran_no")
     private String payMerTranNo;

     /** 交易金额，与原交易金额一致*/
     @JsonProperty("refund_amount")
     private String refundAmount;

     /** 商户编号*/
     @JsonProperty("mer_ptc_id")
     private String merPtcId;

     /** 订单号*/
     @JsonProperty("sys_order_no")
     private String sysOrderNo;

	public String getCancelMerTranNo() {
		return cancelMerTranNo;
	}

	public void setCancelMerTranNo(String cancelMerTranNo) {
		this.cancelMerTranNo = cancelMerTranNo;
	}
	public String getMerTradeDate() {
		return merTradeDate;
	}

	public void setMerTradeDate(String merTradeDate) {
		this.merTradeDate = merTradeDate;
	}
	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getPayMerTranNo() {
		return payMerTranNo;
	}

	public void setPayMerTranNo(String payMerTranNo) {
		this.payMerTranNo = payMerTranNo;
	}
	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getMerPtcId() {
		return merPtcId;
	}

	public void setMerPtcId(String merPtcId) {
		this.merPtcId = merPtcId;
	}
	public String getSysOrderNo() {
		return sysOrderNo;
	}

	public void setSysOrderNo(String sysOrderNo) {
		this.sysOrderNo = sysOrderNo;
	}
}	public ReqHead getReqHead() {
		return reqHead;
	}

	public void setReqHead(ReqHead reqHead) {
		this.reqHead = reqHead;
	}
	public ReqBody getReqBody() {
		return reqBody;
	}

	public void setReqBody(ReqBody reqBody) {
		this.reqBody = reqBody;
	}
}
}