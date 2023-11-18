package com.yiling.hmc.insurance.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 泰康试算结果对象
 */
@Data
public class TrialResult {

    /**
     * 理算结果唯一标识
     */
    private String claimNo;

    /**
     * 子保单号
     */
    private String policyNo;

    /**
     * 团单号
     */
    private String multipleNo;

    /**
     * 渠道服务订单号
     */
    private String channelMainId;

    /**
     * 试算结果
     */
    private Integer claimResultCode;

    /**
     * 理算结果说明
     */
    private String claimResultMsg;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 保险报销金额
     */
    private BigDecimal reimbursementAmount;

    /**
     * 理赔金额
     */
    private BigDecimal claimAmount;
}
