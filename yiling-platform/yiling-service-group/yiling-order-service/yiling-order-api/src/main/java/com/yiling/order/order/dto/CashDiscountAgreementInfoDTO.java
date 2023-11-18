package com.yiling.order.order.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 现折协议信息 DTO
 *
 * @author: xuan.zhou
 * @date: 2021/7/13
 */
@Data
public class CashDiscountAgreementInfoDTO implements java.io.Serializable {

    private static final long serialVersionUID = 8483336844903027818L;

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 版本号
     */
    private String version;

    /**
     * 协议政策
     */
    private BigDecimal policyValue;

    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;
}
