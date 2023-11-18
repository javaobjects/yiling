package com.yiling.user.agreement.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveAgreementConditionRequest extends BaseRequest {

    /**
     * 条件序号可为月,季度,梯度
     */
    private Integer rangeNo;

    /**
     * 条件总额
     */
    private BigDecimal totalAmount;

    /**
     * 返利百分比
     */
    private BigDecimal policyValue;

    /**
     * 政策类型 1-购进额 2-回款额
     */
    private Integer policyType;

    /**
     * 协议政策
     */
    private Integer percentage;

    /**
     * 拆解数额
     */
    private BigDecimal amount;

    /**
     * 时间节点
     */
    private Integer timeNode;

    /**
     * 梯度天数起始值
     */
    private Integer mixValue;

    /**
     * 梯度天数最大值
     */
    private Integer maxValue;

    /**
     * 支付方式（条件支付方式 1-账期支付 2-预付款支付）
     */
    private Integer payType;

    /**
     * 回款方式0全部1指定（回款形式集合 1-电汇 2-银行汇票 3-银行承兑）
     */
    private Integer backAmountType;

    /**
     * 支付方式0全部1指定 （条件支付方式 1-账期支付 2-预付款支付）
     */
    private List<Integer> payTypeValues;

    /**
     * 回款方式0全部1指定（回款形式集合 1-电汇 2-银行汇票 3-银行承兑）
     */
    private List<Integer> backAmountTypeValues;
}
