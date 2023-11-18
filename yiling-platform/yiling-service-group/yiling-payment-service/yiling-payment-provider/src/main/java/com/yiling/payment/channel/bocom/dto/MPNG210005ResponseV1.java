package com.yiling.payment.channel.bocom.dto;

import com.bocom.api.BocomResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MPNG210005ResponseV1 extends BocomResponse {
 /** ""*/
 @JsonProperty("rsp_body")
 private RspBody rspBody;

 public static class RspBody {
  /** 商户交易编号*/
  @JsonProperty("pay_mer_tran_no")
  private String payMerTranNo;

  /** 用于调起SDK或小程序的json列表组合*/
  @JsonProperty("tran_package")
  private String tranPackage;

  /** 交行内部订单号*/
  @JsonProperty("sys_order_no")
  private String sysOrderNo;

 public String getPayMerTranNo() {
     return payMerTranNo;
 }

 public void setPayMerTranNo(String payMerTranNo) {
     this.payMerTranNo = payMerTranNo;
 }
 public String getTranPackage() {
     return tranPackage;
 }

 public void setTranPackage(String tranPackage) {
     this.tranPackage = tranPackage;
 }
 public String getSysOrderNo() {
     return sysOrderNo;
 }

 public void setSysOrderNo(String sysOrderNo) {
     this.sysOrderNo = sysOrderNo;
 }
}	/** ""*/
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