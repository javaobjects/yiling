package com.yiling.payment.channel.bocom.request;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yiling.payment.channel.bocom.dto.MPNG020701ResponseV1;


public class MPNG020701RequestV1 extends AbstractBocomRequest<MPNG020701ResponseV1> {

  @Override
  public Class<MPNG020701ResponseV1> getResponseClass() {
    return MPNG020701ResponseV1.class;
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
    return MPNG020701RequestV1Biz.class;
  }

  public static class MPNG020701RequestV1Biz implements BizContent {

	/** "请求头"*/
	@JsonProperty("req_head")
	private ReqHead reqHead;

	public static class ReqHead {
     /** 交易时间， 格式：yyyymmddhhmmss*/
     @JsonProperty("trans_time")
     private String transTime;

     /** 版本号，默认上送1.0*/
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
}	/** ""*/
	@JsonProperty("req_body")
	private ReqBody reqBody;

	public static class ReqBody {
     /** 退款金额，单位元*/
     @JsonProperty("amount")
     private String amount;

     /** 交易场景，上送原支付交易场景如：B2C-API-DISPLAYCODE*/
     @JsonProperty("tran_scene")
     private String tranScene;

     /** 商户编号*/
     @JsonProperty("mer_ptc_id")
     private String merPtcId;

     /** 后台通知地址*/
     @JsonProperty("notify_url")
     private String notifyUrl;

     /** 原交易商户侧交易日期*/
     @JsonProperty("mer_trade_date")
     private String merTradeDate;

     /** 门店id*/
     @JsonProperty("shop_id")
     private String shopId;

     /** 服务商编号*/
     @JsonProperty("partner_id")
     private String partnerId;

     /** 原支付交易商户交易编号*/
     @JsonProperty("pay_mer_tran_no")
     private String payMerTranNo;

     /** 商户内部备注*/
     @JsonProperty("mer_memo")
     private String merMemo;

     /** 商户退款的交易编号*/
     @JsonProperty("refund_mer_tran_no")
     private String refundMerTranNo;

     /** 币种，境内商户仅支持人民币,固定上送CNY*/
     @JsonProperty("currency")
     private String currency;

     /** 商户侧退款时间 格式：hhmmss*/
     @JsonProperty("mer_refund_time")
     private String merRefundTime;

     /** 商户侧退款日期 格式：yyyyMMdd*/
     @JsonProperty("mer_refund_date")
     private String merRefundDate;

     /** 交易内容，可查询*/
     @JsonProperty("tran_content")
     private String tranContent;

     /** 交行内部订单号，交行内部订单号和商户交易编号二选一，若同时上送优先使用系统订单号*/
     @JsonProperty("sys_order_no")
     private String sysOrderNo;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTranScene() {
		return tranScene;
	}

	public void setTranScene(String tranScene) {
		this.tranScene = tranScene;
	}
	public String getMerPtcId() {
		return merPtcId;
	}

	public void setMerPtcId(String merPtcId) {
		this.merPtcId = merPtcId;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getMerTradeDate() {
		return merTradeDate;
	}

	public void setMerTradeDate(String merTradeDate) {
		this.merTradeDate = merTradeDate;
	}
	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
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
	public String getMerMemo() {
		return merMemo;
	}

	public void setMerMemo(String merMemo) {
		this.merMemo = merMemo;
	}
	public String getRefundMerTranNo() {
		return refundMerTranNo;
	}

	public void setRefundMerTranNo(String refundMerTranNo) {
		this.refundMerTranNo = refundMerTranNo;
	}
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getMerRefundTime() {
		return merRefundTime;
	}

	public void setMerRefundTime(String merRefundTime) {
		this.merRefundTime = merRefundTime;
	}
	public String getMerRefundDate() {
		return merRefundDate;
	}

	public void setMerRefundDate(String merRefundDate) {
		this.merRefundDate = merRefundDate;
	}
	public String getTranContent() {
		return tranContent;
	}

	public void setTranContent(String tranContent) {
		this.tranContent = tranContent;
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