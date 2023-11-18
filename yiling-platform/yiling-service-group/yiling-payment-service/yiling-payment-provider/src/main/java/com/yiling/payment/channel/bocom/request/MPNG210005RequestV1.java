package com.yiling.payment.channel.bocom.request;

import java.util.List;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yiling.payment.channel.bocom.dto.MPNG210005ResponseV1;


public class MPNG210005RequestV1 extends AbstractBocomRequest<MPNG210005ResponseV1> {

  @Override
  public Class<MPNG210005ResponseV1> getResponseClass() {
    return MPNG210005ResponseV1.class;
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
    return MPNG210005RequestV1Biz.class;
  }

  public static class MPNG210005RequestV1Biz implements BizContent {

	/** ""*/
	@JsonProperty("req_head")
	private ReqHead reqHead;

	public static class ReqHead {
     /** 交易时间 yyyymmddhhmmss*/
     @JsonProperty("trans_time")
     private String transTime;

     /** 版本信息*/
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
	/** "商户分账信息"*/
	@JsonProperty("royalty_info")
	private List<RoyaltyInfo> royaltyInfo;

	public static class RoyaltyInfo {
     /** 分账金额*/
     @JsonProperty("amount")
     private String amount;

     /** 分账的序号*/
     @JsonProperty("serial_no")
     private String serialNo;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
}     /** 交易失效时间*/
     @JsonProperty("valid_period")
     private String validPeriod;

     /** 交易场景*/
     @JsonProperty("tran_scene")
     private String tranScene;

     /** ip*/
     @JsonProperty("ip")
     private String ip;

     /** 商户编号*/
     @JsonProperty("mer_ptc_id")
     private String merPtcId;

     /** 商户侧交易时间*/
     @JsonProperty("mer_trade_time")
     private String merTradeTime;

     /** 后台通知地址*/
     @JsonProperty("notify_url")
     private String notifyUrl;

     /** 商户侧交易日期*/
     @JsonProperty("mer_trade_date")
     private String merTradeDate;

     /** 服务商编号*/
     @JsonProperty("partner_id")
     private String partnerId;

     /** 商户交易编号*/
     @JsonProperty("pay_mer_tran_no")
     private String payMerTranNo;

     /** 商户内部备注*/
     @JsonProperty("mer_memo")
     private String merMemo;

     /** 商户APP登记的appid*/
     @JsonProperty("sub_app_id")
     private String subAppId;

     /** 商户订单总金额*/
     @JsonProperty("total_amount")
     private String totalAmount;

     /** 小程序用户OpenID*/
     @JsonProperty("sub_open_id")
     private String subOpenId;

	/** "额外返回的字段"*/
	@JsonProperty("require_fields")
	private List<RequireFields> requireFields;

	public static class RequireFields {
     /** 额外返回的属性*/
     @JsonProperty("require_field")
     private String requireField;

	public String getRequireField() {
		return requireField;
	}

	public void setRequireField(String requireField) {
		this.requireField = requireField;
	}
}     /** 线上或线下*/
     @JsonProperty("location")
     private String location;

     /** 币种*/
     @JsonProperty("currency")
     private String currency;

     /** 交易内容*/
     @JsonProperty("tran_content")
     private String tranContent;

     /** 商户无优惠金额*/
     @JsonProperty("no_dsct_amount")
     private String noDsctAmount;

     /** 禁用付款渠道*/
     @JsonProperty("disable_pay_channels")
     private String disablePayChannels;

     /** 前台跳转地址*/
     @JsonProperty("jump_url")
     private String jumpUrl;

     /** 门店号*/
     @JsonProperty("shop_id")
     private String shopId;

	public List<RoyaltyInfo> getRoyaltyInfo() {
		return royaltyInfo;
	}

	public void setRoyaltyInfo(List<RoyaltyInfo> royaltyInfo) {
		this.royaltyInfo = royaltyInfo;
	}
	public String getValidPeriod() {
		return validPeriod;
	}

	public void setValidPeriod(String validPeriod) {
		this.validPeriod = validPeriod;
	}
	public String getTranScene() {
		return tranScene;
	}

	public void setTranScene(String tranScene) {
		this.tranScene = tranScene;
	}
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMerPtcId() {
		return merPtcId;
	}

	public void setMerPtcId(String merPtcId) {
		this.merPtcId = merPtcId;
	}
	public String getMerTradeTime() {
		return merTradeTime;
	}

	public void setMerTradeTime(String merTradeTime) {
		this.merTradeTime = merTradeTime;
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
	public String getSubAppId() {
		return subAppId;
	}

	public void setSubAppId(String subAppId) {
		this.subAppId = subAppId;
	}
	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getSubOpenId() {
		return subOpenId;
	}

	public void setSubOpenId(String subOpenId) {
		this.subOpenId = subOpenId;
	}
	public List<RequireFields> getRequireFields() {
		return requireFields;
	}

	public void setRequireFields(List<RequireFields> requireFields) {
		this.requireFields = requireFields;
	}
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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
	public String getNoDsctAmount() {
		return noDsctAmount;
	}

	public void setNoDsctAmount(String noDsctAmount) {
		this.noDsctAmount = noDsctAmount;
	}
	public String getDisablePayChannels() {
		return disablePayChannels;
	}

	public void setDisablePayChannels(String disablePayChannels) {
		this.disablePayChannels = disablePayChannels;
	}
	public String getJumpUrl() {
		return jumpUrl;
	}

	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}
	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
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