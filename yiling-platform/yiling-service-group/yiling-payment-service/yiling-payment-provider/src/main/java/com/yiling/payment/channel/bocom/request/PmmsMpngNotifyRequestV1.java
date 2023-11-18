package com.yiling.payment.channel.bocom.request;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yiling.payment.channel.bocom.dto.PmmsMpngNotifyResponseV1;


public class  PmmsMpngNotifyRequestV1 extends AbstractBocomRequest<PmmsMpngNotifyResponseV1> {

  @Override
  public Class<PmmsMpngNotifyResponseV1> getResponseClass() {
    return PmmsMpngNotifyResponseV1.class;
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
    return PmmsMpngNotifyRequestV1Biz.class;
  }

  public static class PmmsMpngNotifyRequestV1Biz implements BizContent {

     /** 该参数必输，为通知第三方的URL.*/
     @JsonProperty("notify_url")
     private String notifyUrl;

     /** 交易类型 
PAY-支付
*/
     @JsonProperty("tran_type")
     private String tranType;

     /** 商户交易编号*/
     @JsonProperty("mer_tran_no")
     private String merTranNo;

     /** 交易状态
SUCCESS：交易成功
FAILURE 交易失败
*/
     @JsonProperty("tran_state")
     private String tranState;

     /** 交易状态码*/
     @JsonProperty("tran_state_code")
     private String tranStateCode;

     /** 交易状态提示*/
     @JsonProperty("tran_state_msg")
     private String tranStateMsg;

     /** 服务商编号*/
     @JsonProperty("partner_id")
     private String partnerId;

     /** 商户编号*/
     @JsonProperty("mer_ptc_id")
     private String merPtcId;

     /** 交易终态时间*/
     @JsonProperty("final_time")
     private String finalTime;

     /** 商户订单总金额*/
     @JsonProperty("total_amount")
     private String totalAmount;

     /** 买家实付金额*/
     @JsonProperty("buyer_pay_amount")
     private String buyerPayAmount;

     /** 第三方活动优惠金额*/
     @JsonProperty("trd_dsct_amount")
     private String trdDsctAmount;

     /** 支付优惠金额*/
     @JsonProperty("pay_dsct_amount")
     private String payDsctAmount;

     /** 交易使用积分*/
     @JsonProperty("points")
     private String points;

     /** 积分抵扣金额*/
     @JsonProperty("points_deduction_amount")
     private String pointsDeductionAmount;

     /** 优惠券抵扣金额*/
     @JsonProperty("coupon_total_amount")
     private String couponTotalAmount;

     /** 分期数*/
     @JsonProperty("instlmt_no")
     private String instlmtNo;

     /** 币种*/
     @JsonProperty("currency")
     private String currency;

     /** 交易内容*/
     @JsonProperty("tran_content")
     private String tranContent;

     /** mer_memo*/
     @JsonProperty("mer_memo")
     private String merMemo;

	/** "需要返回的值"*/
	@JsonProperty("require_values")
	private RequireValues requireValues;

	public static class RequireValues {
     /** 银行端交易流水*/
     @JsonProperty("bank_tran_no")
     private String bankTranNo;

     /** 微信、 
支付宝、
银联
*/
     @JsonProperty("third_party")
     private String thirdParty;

     /** 第三方渠道交易流水号*/
     @JsonProperty("third_party_tran_no")
     private String thirdPartyTranNo;

     /** 微信支付宝详细付款信息*/
     @JsonProperty("payment_info")
     private String paymentInfo;

     /** 微信支付宝详细退款信息*/
     @JsonProperty("refund_info")
     private String refundInfo;

	public String getBankTranNo() {
		return bankTranNo;
	}

	public void setBankTranNo(String bankTranNo) {
		this.bankTranNo = bankTranNo;
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
	public String getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(String paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	public String getRefundInfo() {
		return refundInfo;
	}

	public void setRefundInfo(String refundInfo) {
		this.refundInfo = refundInfo;
	}
}	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public String getMerTranNo() {
		return merTranNo;
	}

	public void setMerTranNo(String merTranNo) {
		this.merTranNo = merTranNo;
	}
	public String getTranState() {
		return tranState;
	}

	public void setTranState(String tranState) {
		this.tranState = tranState;
	}
	public String getTranStateCode() {
		return tranStateCode;
	}

	public void setTranStateCode(String tranStateCode) {
		this.tranStateCode = tranStateCode;
	}
	public String getTranStateMsg() {
		return tranStateMsg;
	}

	public void setTranStateMsg(String tranStateMsg) {
		this.tranStateMsg = tranStateMsg;
	}
	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getMerPtcId() {
		return merPtcId;
	}

	public void setMerPtcId(String merPtcId) {
		this.merPtcId = merPtcId;
	}
	public String getFinalTime() {
		return finalTime;
	}

	public void setFinalTime(String finalTime) {
		this.finalTime = finalTime;
	}
	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getBuyerPayAmount() {
		return buyerPayAmount;
	}

	public void setBuyerPayAmount(String buyerPayAmount) {
		this.buyerPayAmount = buyerPayAmount;
	}
	public String getTrdDsctAmount() {
		return trdDsctAmount;
	}

	public void setTrdDsctAmount(String trdDsctAmount) {
		this.trdDsctAmount = trdDsctAmount;
	}
	public String getPayDsctAmount() {
		return payDsctAmount;
	}

	public void setPayDsctAmount(String payDsctAmount) {
		this.payDsctAmount = payDsctAmount;
	}
	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}
	public String getPointsDeductionAmount() {
		return pointsDeductionAmount;
	}

	public void setPointsDeductionAmount(String pointsDeductionAmount) {
		this.pointsDeductionAmount = pointsDeductionAmount;
	}
	public String getCouponTotalAmount() {
		return couponTotalAmount;
	}

	public void setCouponTotalAmount(String couponTotalAmount) {
		this.couponTotalAmount = couponTotalAmount;
	}
	public String getInstlmtNo() {
		return instlmtNo;
	}

	public void setInstlmtNo(String instlmtNo) {
		this.instlmtNo = instlmtNo;
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
	public String getMerMemo() {
		return merMemo;
	}

	public void setMerMemo(String merMemo) {
		this.merMemo = merMemo;
	}
	public RequireValues getRequireValues() {
		return requireValues;
	}

	public void setRequireValues(RequireValues requireValues) {
		this.requireValues = requireValues;
	}
}
}