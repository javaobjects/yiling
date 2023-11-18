package com.yiling.payment.pay.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.payment.enums.TradeTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建支付订单
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.dto.request
 * @date: 2021/10/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreatePayOrderRequest extends BaseRequest {
    private static final long serialVersionUID = 3285486068016830835L;

    /**
     * 订单
     */
    private List<CreatePayOrderRequest.appOrderRequest> appOrderList;

    /**
     * 交易类型(1:定金,2:支付,3:在线还款,4:尾款,5:会员,6:问诊,7:处方) {@link TradeTypeEnum}
     */
    private TradeTypeEnum tradeType;

    /**
     * 交易描述
     */
    private String content;

    @Data
    public static class appOrderRequest extends BaseRequest {
        private static final long serialVersionUID = 3285486068046840837L;
        /**
         * 订单ID
         */
        private Long appOrderId;

        /**
         * 订单号
         */
        private String appOrderNo;

        /**
         * 支付金额
         */
        private BigDecimal amount;

        /**
         * 支付人用户ID
         */
        private Long userId;

        /**
         * 买家Eid
         */
        private Long buyerEid;

        /**
         * 买家Eid
         */
        private Long sellerEid;
    }




}
