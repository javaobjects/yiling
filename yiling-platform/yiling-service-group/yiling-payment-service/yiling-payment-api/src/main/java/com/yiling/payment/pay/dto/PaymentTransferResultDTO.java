package com.yiling.payment.pay.dto;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.payment.enums.TradeStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.dto
 * @date: 2021/11/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PaymentTransferResultDTO extends BaseDTO {

    /**
     * 业务ID
     */
    private Long businessId;
    /**
     * 企业付款订单号
     */
    private String payNo;

    /**
     * 支付公司返回的付款订单号
     */
    private String thirdTradeNo;

    /**
     * 发起付款是否成功
     */
    private Boolean createStatus;

    /**
     * 失败原因
     */
    private String errMSg;

    /**
     * 交易类型
     */
    private Integer tradeType;


    /**
     * 交易状态
     */
    private TradeStatusEnum tradeStatusEnum;

}
