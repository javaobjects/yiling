package com.yiling.payment.channel.bocom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.bocom.api.BocomResponse;
import java.util.List;
public class MPNG020702ResponseV1 extends BocomResponse {
    /** ""*/
    @JsonProperty("rsp_body")
    private RspBody rspBody;

    public static class RspBody {
        /** 支付账户类型*/
        @JsonProperty("account_type")
        private String accountType;

        /** ""*/
        @JsonProperty("goods_info")
        private GoodsInfo goodsInfo;

        public static class GoodsInfo {
            /** 商品名称*/
            @JsonProperty("goods_name")
            private String goodsName;

            /** 商品简称*/
            @JsonProperty("goods_txt")
            private String goodsTxt;

            /** 商品详情*/
            @JsonProperty("goods_desc")
            private String goodsDesc;

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }
            public String getGoodsTxt() {
                return goodsTxt;
            }

            public void setGoodsTxt(String goodsTxt) {
                this.goodsTxt = goodsTxt;
            }
            public String getGoodsDesc() {
                return goodsDesc;
            }

            public void setGoodsDesc(String goodsDesc) {
                this.goodsDesc = goodsDesc;
            }
        }	/** ""*/
        @JsonProperty("memo_info")
        private MemoInfo memoInfo;

        public static class MemoInfo {
            /** 平台商备注*/
            @JsonProperty("plat_memo")
            private String platMemo;

            /** 卖家备注*/
            @JsonProperty("seller_memo")
            private String sellerMemo;

            /** 付款备注*/
            @JsonProperty("pay_memo")
            private String payMemo;

            /** 买家备注*/
            @JsonProperty("buyer_memo")
            private String buyerMemo;

            /** B2B授权超时时间*/
            @JsonProperty("accr_time_out")
            private String accrTimeOut;

            public String getPlatMemo() {
                return platMemo;
            }

            public void setPlatMemo(String platMemo) {
                this.platMemo = platMemo;
            }
            public String getSellerMemo() {
                return sellerMemo;
            }

            public void setSellerMemo(String sellerMemo) {
                this.sellerMemo = sellerMemo;
            }
            public String getPayMemo() {
                return payMemo;
            }

            public void setPayMemo(String payMemo) {
                this.payMemo = payMemo;
            }
            public String getBuyerMemo() {
                return buyerMemo;
            }

            public void setBuyerMemo(String buyerMemo) {
                this.buyerMemo = buyerMemo;
            }
            public String getAccrTimeOut() {
                return accrTimeOut;
            }

            public void setAccrTimeOut(String accrTimeOut) {
                this.accrTimeOut = accrTimeOut;
            }
        }     /** 交易使用积分*/
        @JsonProperty("points")
        private String points;

        /** INITIAL：初始化*/
        @JsonProperty("order_status")
        private String orderStatus;

        /** 商户内部备注*/
        @JsonProperty("mer_memo")
        private String merMemo;

        /** ""*/
        @JsonProperty("user_info")
        private UserInfo userInfo;

        public static class UserInfo {
            /** 卖家昵称*/
            @JsonProperty("seller_name")
            private String sellerName;

            /** 买家昵称*/
            @JsonProperty("buyer_name")
            private String buyerName;

            /** 买家会员编号*/
            @JsonProperty("buyer_id")
            private String buyerId;

            /** 卖家id*/
            @JsonProperty("seller_id")
            private String sellerId;

            public String getSellerName() {
                return sellerName;
            }

            public void setSellerName(String sellerName) {
                this.sellerName = sellerName;
            }
            public String getBuyerName() {
                return buyerName;
            }

            public void setBuyerName(String buyerName) {
                this.buyerName = buyerName;
            }
            public String getBuyerId() {
                return buyerId;
            }

            public void setBuyerId(String buyerId) {
                this.buyerId = buyerId;
            }
            public String getSellerId() {
                return sellerId;
            }

            public void setSellerId(String sellerId) {
                this.sellerId = sellerId;
            }
        }     /** 第三方活动优惠金额*/
        @JsonProperty("trd_dsct_amount")
        private String trdDsctAmount;

        /** ""*/
        @JsonProperty("require_values")
        private RequireValues requireValues;

        public static class RequireValues {
            /** 无描述*/
            @JsonProperty("bank_tran_no")
            private String bankTranNo;

            /** 无描述*/
            @JsonProperty("open_id")
            private String openId;

            /** 无描述*/
            @JsonProperty("sub_openid")
            private String subOpenid;

            /** 无描述*/
            @JsonProperty("third_party")
            private String thirdParty;

            @JsonProperty("third_party_tran_no")
            private String thirdPartyTranNo;

            public String getBankTranNo() {
                return bankTranNo;
            }

            public void setBankTranNo(String bankTranNo) {
                this.bankTranNo = bankTranNo;
            }

            public String getOpenId() {
                return openId;
            }

            public void setOpenId(String openId) {
                this.openId = openId;
            }

            public String getsubOpenid() {
                return subOpenid;
            }

            public void setsubOpenid(String subOpenid) {
                this.subOpenid = subOpenid;
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
        }	/** ""*/
        @JsonProperty("mer_info")
        private MerInfo merInfo;

        public static class MerInfo {
            /** 商户编号*/
            @JsonProperty("mer_ptc_id")
            private String merPtcId;

            /** 商户简称*/
            @JsonProperty("mer_name_cn")
            private String merNameCn;

            /** 商户所属分行*/
            @JsonProperty("mer_open_branch")
            private String merOpenBranch;

            public String getMerPtcId() {
                return merPtcId;
            }

            public void setMerPtcId(String merPtcId) {
                this.merPtcId = merPtcId;
            }
            public String getMerNameCn() {
                return merNameCn;
            }

            public void setMerNameCn(String merNameCn) {
                this.merNameCn = merNameCn;
            }
            public String getMerOpenBranch() {
                return merOpenBranch;
            }

            public void setMerOpenBranch(String merOpenBranch) {
                this.merOpenBranch = merOpenBranch;
            }
        }     /** PROCESS：处理中*/
        @JsonProperty("tran_state")
        private String tranState;

        /** 币种目前只支持CNY*/
        @JsonProperty("currency")
        private String currency;

        /** 买家实付金额*/
        @JsonProperty("buyer_pay_amount")
        private String buyerPayAmount;

        /** 订单号*/
        @JsonProperty("sys_order_no")
        private String sysOrderNo;

        /** 交易状态码*/
        @JsonProperty("tran_state_code")
        private String tranStateCode;

        /** 交易状态提示*/
        @JsonProperty("tran_state_msg")
        private String tranStateMsg;

        /** ""*/
        @JsonProperty("pay_detail_info")
        private List<PayDetailInfo> payDetailInfo;

        public static class PayDetailInfo {
            /** 付款日期*/
            @JsonProperty("pay_date")
            private String payDate;

            /** 流水状态*/
            @JsonProperty("pay_state")
            private String payState;

            /** 流水金额*/
            @JsonProperty("pay_amt")
            private String payAmt;

            /** 银行流水备注*/
            @JsonProperty("pay_bank_memo")
            private String payBankMemo;

            /** 所属银行名称*/
            @JsonProperty("pay_bank_name")
            private String payBankName;

            /** 银行交易流水号*/
            @JsonProperty("pay_no")
            private String payNo;

            /** 商户流水备注*/
            @JsonProperty("pay_memo")
            private String payMemo;

            /** 流水类型*/
            @JsonProperty("pay_type")
            private String payType;

            /** 支付类型*/
            @JsonProperty("channel_type")
            private String channelType;

            /** 商户流水号*/
            @JsonProperty("mer_tran_serial_no")
            private String merTranSerialNo;

            public String getPayDate() {
                return payDate;
            }

            public void setPayDate(String payDate) {
                this.payDate = payDate;
            }
            public String getPayState() {
                return payState;
            }

            public void setPayState(String payState) {
                this.payState = payState;
            }
            public String getPayAmt() {
                return payAmt;
            }

            public void setPayAmt(String payAmt) {
                this.payAmt = payAmt;
            }
            public String getPayBankMemo() {
                return payBankMemo;
            }

            public void setPayBankMemo(String payBankMemo) {
                this.payBankMemo = payBankMemo;
            }
            public String getPayBankName() {
                return payBankName;
            }

            public void setPayBankName(String payBankName) {
                this.payBankName = payBankName;
            }
            public String getPayNo() {
                return payNo;
            }

            public void setPayNo(String payNo) {
                this.payNo = payNo;
            }
            public String getPayMemo() {
                return payMemo;
            }

            public void setPayMemo(String payMemo) {
                this.payMemo = payMemo;
            }
            public String getPayType() {
                return payType;
            }

            public void setPayType(String payType) {
                this.payType = payType;
            }
            public String getChannelType() {
                return channelType;
            }

            public void setChannelType(String channelType) {
                this.channelType = channelType;
            }
            public String getMerTranSerialNo() {
                return merTranSerialNo;
            }

            public void setMerTranSerialNo(String merTranSerialNo) {
                this.merTranSerialNo = merTranSerialNo;
            }
        }     /** 商户交易编号*/
        @JsonProperty("pay_mer_tran_no")
        private String payMerTranNo;

        /** 商户订单总金额*/
        @JsonProperty("total_amount")
        private String totalAmount;

        /** 商户已退款金额*/
        @JsonProperty("refunded_amt")
        private String refundedAmt;

        /** 交易内容*/
        @JsonProperty("tran_content")
        private String tranContent;

        /** 支付优惠金额*/
        @JsonProperty("pay_dsct_amount")
        private String payDsctAmount;

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }
        public GoodsInfo getGoodsInfo() {
            return goodsInfo;
        }

        public void setGoodsInfo(GoodsInfo goodsInfo) {
            this.goodsInfo = goodsInfo;
        }
        public MemoInfo getMemoInfo() {
            return memoInfo;
        }

        public void setMemoInfo(MemoInfo memoInfo) {
            this.memoInfo = memoInfo;
        }
        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }
        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }
        public String getMerMemo() {
            return merMemo;
        }

        public void setMerMemo(String merMemo) {
            this.merMemo = merMemo;
        }
        public UserInfo getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
        }
        public String getTrdDsctAmount() {
            return trdDsctAmount;
        }

        public void setTrdDsctAmount(String trdDsctAmount) {
            this.trdDsctAmount = trdDsctAmount;
        }
        public RequireValues getRequireValues() {
            return requireValues;
        }

        public void setRequireValues(RequireValues requireValues) {
            this.requireValues = requireValues;
        }
        public MerInfo getMerInfo() {
            return merInfo;
        }

        public void setMerInfo(MerInfo merInfo) {
            this.merInfo = merInfo;
        }
        public String getTranState() {
            return tranState;
        }

        public void setTranState(String tranState) {
            this.tranState = tranState;
        }
        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
        public String getBuyerPayAmount() {
            return buyerPayAmount;
        }

        public void setBuyerPayAmount(String buyerPayAmount) {
            this.buyerPayAmount = buyerPayAmount;
        }
        public String getSysOrderNo() {
            return sysOrderNo;
        }

        public void setSysOrderNo(String sysOrderNo) {
            this.sysOrderNo = sysOrderNo;
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
        public List<PayDetailInfo> getPayDetailInfo() {
            return payDetailInfo;
        }

        public void setPayDetailInfo(List<PayDetailInfo> payDetailInfo) {
            this.payDetailInfo = payDetailInfo;
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
        public String getRefundedAmt() {
            return refundedAmt;
        }

        public void setRefundedAmt(String refundedAmt) {
            this.refundedAmt = refundedAmt;
        }
        public String getTranContent() {
            return tranContent;
        }

        public void setTranContent(String tranContent) {
            this.tranContent = tranContent;
        }
        public String getPayDsctAmount() {
            return payDsctAmount;
        }

        public void setPayDsctAmount(String payDsctAmount) {
            this.payDsctAmount = payDsctAmount;
        }
    }	/** ""*/
    @JsonProperty("rsp_head")
    private RspHead rspHead;

    public static class RspHead {
        /** 交易码*/
        @JsonProperty("trans_code")
        private String transCode;

        /** 响应码*/
        @JsonProperty("response_code")
        private String responseCode;

        /** 交易状态*/
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