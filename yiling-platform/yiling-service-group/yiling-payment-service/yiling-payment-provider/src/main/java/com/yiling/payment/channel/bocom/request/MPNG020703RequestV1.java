package com.yiling.payment.channel.bocom.request;

import com.bocom.api.AbstractBocomRequest;
import com.bocom.api.BizContent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yiling.payment.channel.bocom.dto.MPNG020703ResponseV1;


public class MPNG020703RequestV1 extends AbstractBocomRequest<MPNG020703ResponseV1> {

    @Override
    public Class<MPNG020703ResponseV1> getResponseClass() {
        return MPNG020703ResponseV1.class;
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
        return MPNG020703RequestV1Biz.class;
    }

    public static class MPNG020703RequestV1Biz implements BizContent {

        /** ""*/
        @JsonProperty("req_head")
        private ReqHead reqHead;

        public static class ReqHead {
            /** 交易时间 yyyymmddhhmmss*/
            @JsonProperty("trans_time")
            private String transTime;

            /** 版本号*/
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
            /** 服务商编号*/
            @JsonProperty("partner_id")
            private String partnerId;

            /** 交易场景，原支付交易的场景：如B2C-API-DISPLAYCODE*/
            @JsonProperty("tran_scene")
            private String tranScene;

            /** 商户退款的交易编号，需确保当日不重复*/
            @JsonProperty("refund_mer_tran_no")
            private String refundMerTranNo;

            /** 商户编号*/
            @JsonProperty("mer_ptc_id")
            private String merPtcId;

            /** 商户侧退款日期，格式：hhmmss*/
            @JsonProperty("mer_refund_date")
            private String merRefundDate;

            /** 交行内部订单号，交行内部订单号和商户交易编号二选一，若送了优先使用系统订单号查询*/
            @JsonProperty("sys_order_no")
            private String sysOrderNo;

            public String getPartnerId() {
                return partnerId;
            }

            public void setPartnerId(String partnerId) {
                this.partnerId = partnerId;
            }
            public String getTranScene() {
                return tranScene;
            }

            public void setTranScene(String tranScene) {
                this.tranScene = tranScene;
            }
            public String getRefundMerTranNo() {
                return refundMerTranNo;
            }

            public void setRefundMerTranNo(String refundMerTranNo) {
                this.refundMerTranNo = refundMerTranNo;
            }
            public String getMerPtcId() {
                return merPtcId;
            }

            public void setMerPtcId(String merPtcId) {
                this.merPtcId = merPtcId;
            }
            public String getMerRefundDate() {
                return merRefundDate;
            }

            public void setMerRefundDate(String merRefundDate) {
                this.merRefundDate = merRefundDate;
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