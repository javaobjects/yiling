package com.yiling.payment.channel.bocom.request;

import java.util.List;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yiling.payment.channel.bocom.dto.MPNG020702ResponseV1;


public class MPNG020702RequestV1 extends AbstractBocomRequest<MPNG020702ResponseV1> {

  @Override
  public Class<MPNG020702ResponseV1> getResponseClass() {
    return MPNG020702ResponseV1.class;
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
    return MPNG020702RequestV1Biz.class;
  }

  public static class MPNG020702RequestV1Biz implements BizContent {

	/** ""*/
	@JsonProperty("req_head")
	private ReqHead reqHead;

	public static class ReqHead {
     /** 交易时间 yyyymmddhhmmss*/
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
     /** 原交易商户侧交易日期 yyyyMMdd*/
     @JsonProperty("mer_trade_date")
     private String merTradeDate;

     /** 服务商编号*/
     @JsonProperty("partner_id")
     private String partnerId;

     /** 商户交易编号，商户自定义的订单号，当日不可重复*/
     @JsonProperty("pay_mer_tran_no")
     private String payMerTranNo;

     /** 交易场景，支付交易上送的交易场景，如B2C-API-DISPLAYCODE等*/
     @JsonProperty("tran_scene")
     private String tranScene;

	/** "目前支持的字段包括银行端交易流水号bank_tran_no、第三方渠道third_party、第三方渠道交易流水号third_party_tran_no、微信支付宝详细付款信息payment_info、微信支付宝详细付款信息refund_info"*/
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
}     /** 商户编号*/
     @JsonProperty("mer_ptc_id")
     private String merPtcId;

     /** 交行系统订单号，订单号和商户交易编号二选一，若送了优先使用系统订单号查询*/
     @JsonProperty("sys_order_no")
     private String sysOrderNo;

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
	public String getTranScene() {
		return tranScene;
	}

	public void setTranScene(String tranScene) {
		this.tranScene = tranScene;
	}
	public List<RequireFields> getRequireFields() {
		return requireFields;
	}

	public void setRequireFields(List<RequireFields> requireFields) {
		this.requireFields = requireFields;
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