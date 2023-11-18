package com.yiling.payment.pay.dto.request;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.payment.enums.PayChannelEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业打款参数
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.dto.request
 * @date: 2021/11/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreatePaymentTransferRequest extends BaseRequest {


    /**
     * 交易类型(1:结算..)
     */
    @NotNull
    private Integer tradeType = 1;

    /**
     * 支付方式，
     */
    private String payWay = PayChannelEnum.YEEPAY.getCode();

    /**
     * 支付来源，目前可不传
     */
    private String paySource;

    /**
     * 打款信息
     */
    @NotNull
    private List<PaymentTransferRequest> transferRequestList;


    @Data
    @EqualsAndHashCode(callSuper = true)
    @Accessors(chain = true)
    public static class PaymentTransferRequest extends BaseRequest{

        /**
         * 商家EID
         */
        private Long sellerEid;

        /**
         * 打款企业ID
         */
        private Long eid;

        /**
         * 企业付款订单号
         */
        private Long  businessId;

        /**
         * 付款金额
         */
        private BigDecimal amount;

        /**
         * 支付渠道：1-易宝
         */
        private Integer payChannel;

        /**
         * 手续费承担方： 1-平台 2-用户
         */
        private Integer feeChargeSide;

        /**
         * 到账类型： 1-实时 2-两小时到账 3-次日到账
         */
        private Integer receiveType;
        /**
         * 企业收款账户id
         */
        private Long receiptAccountId;

        /**
         * 收款银行卡号
         */
        private String account;

        /**
         * 收款方开户名
         */
        private String accountName;

        /**
         * 开户行编号 示例值：ICBC 详见BankDO
         */
        private String bankCode;

        /**
         * 支行行号 示例值：403859100011 详见BankDO
         */
        private String branchCode;

        /**
         * 交易描述（附言）
         */
        private String content;

    }
}
