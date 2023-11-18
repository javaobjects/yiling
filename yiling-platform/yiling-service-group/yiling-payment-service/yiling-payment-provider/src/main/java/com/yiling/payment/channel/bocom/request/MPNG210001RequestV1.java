package com.yiling.payment.channel.bocom.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import com.yiling.payment.channel.bocom.dto.MPNG210001ResponseV1;

import java.util.List;


public class MPNG210001RequestV1 extends AbstractBocomRequest<MPNG210001ResponseV1> {

  @Override
  public Class<MPNG210001ResponseV1> getResponseClass() {
    return MPNG210001ResponseV1.class;
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
    return MPNG210001RequestV1Biz.class;
  }

  public static class MPNG210001RequestV1Biz implements BizContent {

	/** "req_head"*/
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
     /** 交易失效时间*/
     @JsonProperty("valid_period")
     private String validPeriod;

     /** 纬度*/
     @JsonProperty("latitude")
     private String latitude;

     /** 终端号*/
     @JsonProperty("terminal_info")
     private String terminalInfo;

     /** 经纬度信息*/
     @JsonProperty("location_id")
     private String locationId;

     /** 服务商编号*/
     @JsonProperty("partner_id")
     private String partnerId;

     /** 不带报文的前台跳转地址*/
     @JsonProperty("jump_url")
     private String jumpUrl;

     /** 商户内部备注*/
     @JsonProperty("mer_memo")
     private String merMemo;

	/** "额外返回的属性"*/
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
}     /** 禁用付款渠道*/
     @JsonProperty("disable_pay_channels")
     private String disablePayChannels;

     /** 币种*/
     @JsonProperty("currency")
     private String currency;

	/** "基站信息"*/
	@JsonProperty("location_info")
	private LocationInfo locationInfo;

	public static class LocationInfo {
     /** 电信网络识别码（NID（电信），电信网络识别码,由电信各由地 级分公司分配。每个地级市可能有1到3个NID）*/
     @JsonProperty("telecom_network_id")
     private String telecomnetworkid;

     /** 基站信号3（SIG(移动、联通)， 16 进制）*/
     @JsonProperty("lbs_signal3")
     private String lbssignal3;

     /** 基站信号1（SIG(移动、联通)， 16 进制）*/
     @JsonProperty("lbs_signal1")
     private String lbssignal1;

     /** 基站信号2（SIG(移动、联通)， 16 进制）*/
     @JsonProperty("lbs_signal2")
     private String lbssignal2;

     /** 位置区域码1（LAC(移动、联通)， 16 进制）*/
     @JsonProperty("location_cd1")
     private String locationcd1;

     /** ICCID（SIM卡卡号）*/
     @JsonProperty("icc_id")
     private String iccid;

     /** 位置区域码3（LAC(移动、联通)， 16 进制）*/
     @JsonProperty("location_cd3")
     private String locationcd3;

     /** 电信基站信号（SIG（电信），16进制）*/
     @JsonProperty("telecom_lbs_signal")
     private String telecomlbssignal;

     /** 移动国家代码（基站信息，由国际电联(ITU)统一分配的移动 国家代码（MCC）。 中国为 460）*/
     @JsonProperty("mobile_country_cd")
     private String mobilecountrycd;

     /** 位置区域码2（LAC(移动、联通)， 16 进制）*/
     @JsonProperty("location_cd2")
     private String locationcd2;

     /** 电信系统识别码（SID（电信），电信系统识别码,每个地级市只 有一个 SID）*/
     @JsonProperty("telecom_system_id")
     private String telecomsystemid;

     /** 移动网络代码（基站信息，由国际电联(ITU)统一分配的移动 网络号码（MNC）。 移动： 00、 02、 04、 07；联通： 01、 06、 09； 电信： 03、 05、 11）*/
     @JsonProperty("mobile_network_num")
     private String mobilenetworknum;

     /** 基站编号2（CID(移动、联通)， 16 进制）*/
     @JsonProperty("lbs_num2")
     private String lbsnum2;

     /** 电信基站（BID（电信），电信网络中的小区识别码，等 效于基站）*/
     @JsonProperty("telecom_lbs")
     private String telecomlbs;

     /** 基站编号1（CID(移动、联通)， 16 进制）*/
     @JsonProperty("lbs_num1")
     private String lbsnum1;

     /** 基站编号3（CID(移动、联通)， 16 进制）*/
     @JsonProperty("lbs_num3")
     private String lbsnum3;

	public String getTelecomnetworkid() {
		return telecomnetworkid;
	}

	public void setTelecomnetworkid(String telecomnetworkid) {
		this.telecomnetworkid = telecomnetworkid;
	}
	public String getLbssignal3() {
		return lbssignal3;
	}

	public void setLbssignal3(String lbssignal3) {
		this.lbssignal3 = lbssignal3;
	}
	public String getLbssignal1() {
		return lbssignal1;
	}

	public void setLbssignal1(String lbssignal1) {
		this.lbssignal1 = lbssignal1;
	}
	public String getLbssignal2() {
		return lbssignal2;
	}

	public void setLbssignal2(String lbssignal2) {
		this.lbssignal2 = lbssignal2;
	}
	public String getLocationcd1() {
		return locationcd1;
	}

	public void setLocationcd1(String locationcd1) {
		this.locationcd1 = locationcd1;
	}
	public String getIccid() {
		return iccid;
	}

	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getLocationcd3() {
		return locationcd3;
	}

	public void setLocationcd3(String locationcd3) {
		this.locationcd3 = locationcd3;
	}
	public String getTelecomlbssignal() {
		return telecomlbssignal;
	}

	public void setTelecomlbssignal(String telecomlbssignal) {
		this.telecomlbssignal = telecomlbssignal;
	}
	public String getMobilecountrycd() {
		return mobilecountrycd;
	}

	public void setMobilecountrycd(String mobilecountrycd) {
		this.mobilecountrycd = mobilecountrycd;
	}
	public String getLocationcd2() {
		return locationcd2;
	}

	public void setLocationcd2(String locationcd2) {
		this.locationcd2 = locationcd2;
	}
	public String getTelecomsystemid() {
		return telecomsystemid;
	}

	public void setTelecomsystemid(String telecomsystemid) {
		this.telecomsystemid = telecomsystemid;
	}
	public String getMobilenetworknum() {
		return mobilenetworknum;
	}

	public void setMobilenetworknum(String mobilenetworknum) {
		this.mobilenetworknum = mobilenetworknum;
	}
	public String getLbsnum2() {
		return lbsnum2;
	}

	public void setLbsnum2(String lbsnum2) {
		this.lbsnum2 = lbsnum2;
	}
	public String getTelecomlbs() {
		return telecomlbs;
	}

	public void setTelecomlbs(String telecomlbs) {
		this.telecomlbs = telecomlbs;
	}
	public String getLbsnum1() {
		return lbsnum1;
	}

	public void setLbsnum1(String lbsnum1) {
		this.lbsnum1 = lbsnum1;
	}
	public String getLbsnum3() {
		return lbsnum3;
	}

	public void setLbsnum3(String lbsnum3) {
		this.lbsnum3 = lbsnum3;
	}
}     /** 经度*/
     @JsonProperty("longitude")
     private String longitude;

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
}     /** 交易场景*/
     @JsonProperty("tran_scene")
     private String tranScene;

     /** 终端IP*/
     @JsonProperty("ip")
     private String ip;

     /** 商户编号*/
     @JsonProperty("mer_ptc_id")
     private String merPtcId;

	/** "终端信息"*/
	@JsonProperty("term_info")
	private TermInfo termInfo;

	public static class TermInfo {
     /** 设备类型*/
     @JsonProperty("device_type")
     private String devicetype;

     /** 密文数据*/
     @JsonProperty("secret_text")
     private String secrettext;

     /** 应用程序版本号*/
     @JsonProperty("app_version")
     private String appversion;

     /** 终端序列号*/
     @JsonProperty("serial_num")
     private String serialnum;

     /** 加密随机因子*/
     @JsonProperty("encrypt_rand_num")
     private String encryptrandnum;

     /** 终端入网认证编号*/
     @JsonProperty("network_license")
     private String networklicense;

	public String getDevicetype() {
		return devicetype;
	}

	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}
	public String getSecrettext() {
		return secrettext;
	}

	public void setSecrettext(String secrettext) {
		this.secrettext = secrettext;
	}
	public String getAppversion() {
		return appversion;
	}

	public void setAppversion(String appversion) {
		this.appversion = appversion;
	}
	public String getSerialnum() {
		return serialnum;
	}

	public void setSerialnum(String serialnum) {
		this.serialnum = serialnum;
	}
	public String getEncryptrandnum() {
		return encryptrandnum;
	}

	public void setEncryptrandnum(String encryptrandnum) {
		this.encryptrandnum = encryptrandnum;
	}
	public String getNetworklicense() {
		return networklicense;
	}

	public void setNetworklicense(String networklicense) {
		this.networklicense = networklicense;
	}
}     /** 商户侧交易时间*/
     @JsonProperty("mer_trade_time")
     private String merTradeTime;

     /** 后台通知地址*/
     @JsonProperty("notify_url")
     private String notifyUrl;

     /** 商户侧交易日期*/
     @JsonProperty("mer_trade_date")
     private String merTradeDate;

     /** 门店编号*/
     @JsonProperty("shop_id")
     private String shopId;

	/** "服务商分润信息"*/
	@JsonProperty("fee_split_info")
	private FeeSplitInfo feeSplitInfo;

	public static class FeeSplitInfo {
     /** 服务商分润金额*/
     @JsonProperty("partner_amount")
     private String partnerAmount;

     /** 商户结算金额*/
     @JsonProperty("mer_amount")
     private String merAmount;

	public String getPartnerAmount() {
		return partnerAmount;
	}

	public void setPartnerAmount(String partnerAmount) {
		this.partnerAmount = partnerAmount;
	}
	public String getMerAmount() {
		return merAmount;
	}

	public void setMerAmount(String merAmount) {
		this.merAmount = merAmount;
	}
}     /** 商户交易编号*/
     @JsonProperty("pay_mer_tran_no")
     private String payMerTranNo;

     /** 商户订单总金额*/
     @JsonProperty("total_amount")
     private String totalAmount;

     /** 线上或线下*/
     @JsonProperty("location")
     private String location;

     /** 交易内容*/
     @JsonProperty("tran_content")
     private String tranContent;

	public String getValidPeriod() {
		return validPeriod;
	}

	public void setValidPeriod(String validPeriod) {
		this.validPeriod = validPeriod;
	}
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getTerminalInfo() {
		return terminalInfo;
	}

	public void setTerminalInfo(String terminalInfo) {
		this.terminalInfo = terminalInfo;
	}
	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getJumpUrl() {
		return jumpUrl;
	}

	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}
	public String getMerMemo() {
		return merMemo;
	}

	public void setMerMemo(String merMemo) {
		this.merMemo = merMemo;
	}
	public List<RequireFields> getRequireFields() {
		return requireFields;
	}

	public void setRequireFields(List<RequireFields> requireFields) {
		this.requireFields = requireFields;
	}
	public String getDisablePayChannels() {
		return disablePayChannels;
	}

	public void setDisablePayChannels(String disablePayChannels) {
		this.disablePayChannels = disablePayChannels;
	}
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public LocationInfo getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(LocationInfo locationInfo) {
		this.locationInfo = locationInfo;
	}
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public List<RoyaltyInfo> getRoyaltyInfo() {
		return royaltyInfo;
	}

	public void setRoyaltyInfo(List<RoyaltyInfo> royaltyInfo) {
		this.royaltyInfo = royaltyInfo;
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
	public TermInfo getTermInfo() {
		return termInfo;
	}

	public void setTermInfo(TermInfo termInfo) {
		this.termInfo = termInfo;
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
	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public FeeSplitInfo getFeeSplitInfo() {
		return feeSplitInfo;
	}

	public void setFeeSplitInfo(FeeSplitInfo feeSplitInfo) {
		this.feeSplitInfo = feeSplitInfo;
	}
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
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	public String getTranContent() {
		return tranContent;
	}

	public void setTranContent(String tranContent) {
		this.tranContent = tranContent;
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