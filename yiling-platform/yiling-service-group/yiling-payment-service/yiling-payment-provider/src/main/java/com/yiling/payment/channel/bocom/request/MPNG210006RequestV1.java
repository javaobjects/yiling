package com.yiling.payment.channel.bocom.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import com.yiling.payment.channel.bocom.dto.MPNG210006ResponseV1;

import java.util.List;


public class MPNG210006RequestV1 extends AbstractBocomRequest<MPNG210006ResponseV1> {

  @Override
  public Class<MPNG210006ResponseV1> getResponseClass() {
    return MPNG210006ResponseV1.class;
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
    return MPNG210006RequestV1Biz.class;
  }

  public static class MPNG210006RequestV1Biz implements BizContent {

	/** "请求报文头"*/
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
}	/** "请求报文体"*/
	@JsonProperty("req_body")
	private ReqBody reqBody;

	public static class ReqBody {
     /** 交易失效时间*/
     @JsonProperty("valid_period")
     private String validPeriod;

     /** 纬度（该字段必输）*/
     @JsonProperty("latitude")
     private String latitude;

     /** 付款码文本*/
     @JsonProperty("scan_code_text")
     private String scanCodeText;

     /** 终端号（该字段必输）*/
     @JsonProperty("terminal_info")
     private String terminalInfo;

     /** 服务商编号*/
     @JsonProperty("partner_id")
     private String partnerId;

     /** 商户内部备注*/
     @JsonProperty("mer_memo")
     private String merMemo;

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
}     /** 币种*/
     @JsonProperty("currency")
     private String currency;

	/** "基站信息"*/
	@JsonProperty("location_info")
	private LocationInfo locationInfo;

	public static class LocationInfo {
     /** 电信网络识别码*/
     @JsonProperty("telecom_network_id")
     private String telecomNetworkId;

     /** 基站信号3(SIG(移动、联通)， 16 进制)*/
     @JsonProperty("lbs_signal3")
     private String lbsSignal3;

     /** 基站信号1（SIG(移动、联通)， 16 进制）*/
     @JsonProperty("lbs_signal1")
     private String lbsSignal1;

     /** 基站信号2（SIG(移动、联通)， 16 进制）*/
     @JsonProperty("lbs_signal2")
     private String lbsSignal2;

     /** 位置区域码1（LAC(移动、联通)， 16 进制）*/
     @JsonProperty("location_cd1")
     private String locationCd1;

     /** ICCID（SIM卡卡号）*/
     @JsonProperty("icc_id")
     private String iccId;

     /** 位置区域码3（LAC(移动、联通)， 16 进制）*/
     @JsonProperty("location_cd3")
     private String locationCd3;

     /** 电信基站信号（SIG（电信），16进制）*/
     @JsonProperty("telecom_lbs_signal")
     private String telecomLbsSignal;

     /** 移动国家代码（基站信息，由国际电联(ITU)统一分配的移动 国家代码（MCC）。 中国为 460）*/
     @JsonProperty("mobile_country_cd")
     private String mobileCountryCd;

     /** 位置区域码2（LAC(移动、联通)， 16 进制）*/
     @JsonProperty("location_cd2")
     private String locationCd2;

     /** 电信系统识别码（NID（电信），电信网络识别码,由电信各由地 级分公司分配。每个地级市可能有1到3个NID）*/
     @JsonProperty("telecom_system_id")
     private String telecomSystemId;

     /** 移动网络代码（基站信息，由国际电联(ITU)统一分配的移动 网络号码（MNC）。 移动： 00、 02、 04、 07；联通： 01、 06、 09； 电信： 03、 05、 11）*/
     @JsonProperty("mobile_network_num")
     private String mobileNetworkNum;

     /** 基站编号2（CID(移动、联通)， 16 进制）*/
     @JsonProperty("lbs_num2")
     private String lbsNum2;

     /** 电信基站（BID（电信），电信网络中的小区识别码，等 效于基站）*/
     @JsonProperty("telecom_lbs")
     private String telecomLbs;

     /** 基站编号1（CID(移动、联通)， 16 进制）*/
     @JsonProperty("lbs_num1")
     private String lbsNum1;

     /** 基站编号3（CID(移动、联通)， 16 进制）*/
     @JsonProperty("lbs_num3")
     private String lbsNum3;

	public String getTelecomNetworkId() {
		return telecomNetworkId;
	}

	public void setTelecomNetworkId(String telecomNetworkId) {
		this.telecomNetworkId = telecomNetworkId;
	}
	public String getLbsSignal3() {
		return lbsSignal3;
	}

	public void setLbsSignal3(String lbsSignal3) {
		this.lbsSignal3 = lbsSignal3;
	}
	public String getLbsSignal1() {
		return lbsSignal1;
	}

	public void setLbsSignal1(String lbsSignal1) {
		this.lbsSignal1 = lbsSignal1;
	}
	public String getLbsSignal2() {
		return lbsSignal2;
	}

	public void setLbsSignal2(String lbsSignal2) {
		this.lbsSignal2 = lbsSignal2;
	}
	public String getLocationCd1() {
		return locationCd1;
	}

	public void setLocationCd1(String locationCd1) {
		this.locationCd1 = locationCd1;
	}
	public String getIccId() {
		return iccId;
	}

	public void setIccId(String iccId) {
		this.iccId = iccId;
	}
	public String getLocationCd3() {
		return locationCd3;
	}

	public void setLocationCd3(String locationCd3) {
		this.locationCd3 = locationCd3;
	}
	public String getTelecomLbsSignal() {
		return telecomLbsSignal;
	}

	public void setTelecomLbsSignal(String telecomLbsSignal) {
		this.telecomLbsSignal = telecomLbsSignal;
	}
	public String getMobileCountryCd() {
		return mobileCountryCd;
	}

	public void setMobileCountryCd(String mobileCountryCd) {
		this.mobileCountryCd = mobileCountryCd;
	}
	public String getLocationCd2() {
		return locationCd2;
	}

	public void setLocationCd2(String locationCd2) {
		this.locationCd2 = locationCd2;
	}
	public String getTelecomSystemId() {
		return telecomSystemId;
	}

	public void setTelecomSystemId(String telecomSystemId) {
		this.telecomSystemId = telecomSystemId;
	}
	public String getMobileNetworkNum() {
		return mobileNetworkNum;
	}

	public void setMobileNetworkNum(String mobileNetworkNum) {
		this.mobileNetworkNum = mobileNetworkNum;
	}
	public String getLbsNum2() {
		return lbsNum2;
	}

	public void setLbsNum2(String lbsNum2) {
		this.lbsNum2 = lbsNum2;
	}
	public String getTelecomLbs() {
		return telecomLbs;
	}

	public void setTelecomLbs(String telecomLbs) {
		this.telecomLbs = telecomLbs;
	}
	public String getLbsNum1() {
		return lbsNum1;
	}

	public void setLbsNum1(String lbsNum1) {
		this.lbsNum1 = lbsNum1;
	}
	public String getLbsNum3() {
		return lbsNum3;
	}

	public void setLbsNum3(String lbsNum3) {
		this.lbsNum3 = lbsNum3;
	}
}     /** 经度（该字段必输）*/
     @JsonProperty("longitude")
     private String longitude;

     /** 终端批次号*/
     @JsonProperty("term_batch_no")
     private String termBatchNo;

     /** 交易场景*/
     @JsonProperty("tran_scene")
     private String tranScene;

     /** ip*/
     @JsonProperty("ip")
     private String ip;

	/** "附加交易信息"*/
	@JsonProperty("addi_trade_data")
	private AddiTradeData addiTradeData;

	public static class AddiTradeData {
     /** 用法标识*/
     @JsonProperty("method")
     private String method;

	/** "用法取值"*/
	@JsonProperty("value")
	private Value value;

	public static class Value {
     /** 预留字段1*/
     @JsonProperty("reverse1")
     private String reverse1;

     /** 证件号*/
     @JsonProperty("card_no")
     private String cardNo;

     /** 持卡人姓名*/
     @JsonProperty("name")
     private String name;

     /** 付款人信息*/
     @JsonProperty("dynamic_token_out_biz_no")
     private String dynamicTokenOutBizNo;

     /** 证件类型  */
     @JsonProperty("card_type")
     private String cardType;

     /** 预留字段2*/
     @JsonProperty("reverse2")
     private String reverse2;

     /** 预留字段3*/
     @JsonProperty("reverse3")
     private String reverse3;

     /** 预留字段4*/
     @JsonProperty("reverse4")
     private String reverse4;

     /** 预留字段5*/
     @JsonProperty("reverse5")
     private String reverse5;

	public String getReverse1() {
		return reverse1;
	}

	public void setReverse1(String reverse1) {
		this.reverse1 = reverse1;
	}
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getDynamicTokenOutBizNo() {
		return dynamicTokenOutBizNo;
	}

	public void setDynamicTokenOutBizNo(String dynamicTokenOutBizNo) {
		this.dynamicTokenOutBizNo = dynamicTokenOutBizNo;
	}
	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getReverse2() {
		return reverse2;
	}

	public void setReverse2(String reverse2) {
		this.reverse2 = reverse2;
	}
	public String getReverse3() {
		return reverse3;
	}

	public void setReverse3(String reverse3) {
		this.reverse3 = reverse3;
	}
	public String getReverse4() {
		return reverse4;
	}

	public void setReverse4(String reverse4) {
		this.reverse4 = reverse4;
	}
	public String getReverse5() {
		return reverse5;
	}

	public void setReverse5(String reverse5) {
		this.reverse5 = reverse5;
	}
}	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}
}     /** 商户编号*/
     @JsonProperty("mer_ptc_id")
     private String merPtcId;

	/** "终端信息（该字段必输）"*/
	@JsonProperty("term_info")
	private TermInfo termInfo;

	public static class TermInfo {
     /** 设备类型（终端设备类型，受理方可参考终端 注册时的设备类型填写，取值如下： 01：自动柜员机（含 ATM 和 CDM） 和多媒体自助终端 02：传统 POS 03： mPOS 04：智能 POS 05： II 型固定电话 06：云闪付终端； 07：保留使用； 08：手机 POS； 09：刷脸付终端；15 10：条码支付受理终端； 11：条码支付辅助受理终端； 12： 行业终端（公交、地铁用于指 定行业的终端）； 13： MIS 终端*/
     @JsonProperty("device_type")
     private String deviceType;

     /** 密文数据（仅在被扫支付类交易报文中出现： 64bit 的密文数据，对终端硬件序列 号和加密随机因子加密后的结果。 本子域取值为： 64bit 密文数据进行 base64 编码后的结果）*/
     @JsonProperty("secret_text")
     private String secretText;

     /** 应用程序版本号应用程序版本号（终端应用程序的版本号。应用程序 变更应保证版本号不重复。 当长度 不足时，右补空格。）*/
     @JsonProperty("app_version")
     private String appVersion;

     /** 终端序列号终端序列号（出现要求： 终端类型（device_type）填写为 02、 03、 04、 05、 06、 08、 09 或 10 时，必须填写终端序列号。）*/
     @JsonProperty("serial_num")
     private String serialNum;

     /** 加密随机因子加密随机因子（仅在被扫支付类交易报文中出现： 若付款码为 19 位数字，则取后 6 位； 若付款码为 EMV 二维码，则取其 tag 57 的卡号/token 号的后 6 位）*/
     @JsonProperty("encrypt_rand_num")
     private String encryptRandNum;

     /** 终端入网认证编号终端入网认证编号（银行卡受理终端产品应用认证编 号。该编号由“中国银联标识产品 企业资质认证办公室”为通过入网 认证的终端进行分配。 银联直连终 端必填。 格式： 5 位字符，例如 P3100）*/
     @JsonProperty("network_license")
     private String networkLicense;

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getSecretText() {
		return secretText;
	}

	public void setSecretText(String secretText) {
		this.secretText = secretText;
	}
	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public String getEncryptRandNum() {
		return encryptRandNum;
	}

	public void setEncryptRandNum(String encryptRandNum) {
		this.encryptRandNum = encryptRandNum;
	}
	public String getNetworkLicense() {
		return networkLicense;
	}

	public void setNetworkLicense(String networkLicense) {
		this.networkLicense = networkLicense;
	}
}     /** 商户侧交易时间*/
     @JsonProperty("mer_trade_time")
     private String merTradeTime;

     /** 终端流水号*/
     @JsonProperty("term_pos_no")
     private String termPosNo;

     /** 商户侧交易日期*/
     @JsonProperty("mer_trade_date")
     private String merTradeDate;

     /** 门店编号*/
     @JsonProperty("shop_id")
     private String shopId;

     /** 商户交易编号*/
     @JsonProperty("pay_mer_tran_no")
     private String payMerTranNo;

     /** 商户订单总金额（元）*/
     @JsonProperty("total_amount")
     private String totalAmount;

     /** 线上或线下*/
     @JsonProperty("location")
     private String location;

     /** 商品详情*/
     @JsonProperty("detail")
     private String detail;

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
	public String getScanCodeText() {
		return scanCodeText;
	}

	public void setScanCodeText(String scanCodeText) {
		this.scanCodeText = scanCodeText;
	}
	public String getTerminalInfo() {
		return terminalInfo;
	}

	public void setTerminalInfo(String terminalInfo) {
		this.terminalInfo = terminalInfo;
	}
	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
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
	public String getTermBatchNo() {
		return termBatchNo;
	}

	public void setTermBatchNo(String termBatchNo) {
		this.termBatchNo = termBatchNo;
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
	public AddiTradeData getAddiTradeData() {
		return addiTradeData;
	}

	public void setAddiTradeData(AddiTradeData addiTradeData) {
		this.addiTradeData = addiTradeData;
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
	public String getTermPosNo() {
		return termPosNo;
	}

	public void setTermPosNo(String termPosNo) {
		this.termPosNo = termPosNo;
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
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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