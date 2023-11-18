package com.yiling.f2b.admin.agreement.form;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
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
public class SaveAgreementConditionForm extends BaseForm {

    /**
     * 条件序号可为月,季度,梯度
     */
    @ApiModelProperty(value ="条件序号可为月,季度,梯度")
    private Integer rangeNo;

    /**
     * 条件总额
     */
    @ApiModelProperty(value ="条件总额")
    private BigDecimal totalAmount;

    /**
     * 协议政策
     */
    @ApiModelProperty(value ="协议政策")
    private BigDecimal policyValue;

    /**
     * 协议政策：1-购进额 2-回款额
     */
    @ApiModelProperty(value = "协议政策：1-购进额 2-回款额")
    private Integer policyType;

    /**
     * 返利百分比
     */
    @ApiModelProperty(value ="拆解百分比，仅在条件类型为月度、季度时需要以数组形式传")
    private Integer percentage;

    /**
     * 拆解数额
     */
    @ApiModelProperty(value ="拆解数额，仅在条件类型为月度、季度时需要以数组形式传")
    private BigDecimal amount;

    /**
     * 时间节点
     */
    @ApiModelProperty(value ="时间节点，仅在条件类型为梯度、时间节点时需要传",example = "28")
    private Integer timeNode;

    /**
     * 梯度天数起始值
     */
    @ApiModelProperty(value ="梯度天数起始值,仅在条件类型为梯度需要以数组形式传")
    private Integer mixValue;

    /**
     * 梯度天数最大值
     */
    @ApiModelProperty(value ="梯度天数最大值,仅在条件类型为梯度需要以数组形式传")
    private Integer maxValue;

    /**
     * 支付方式0全部1指定 （条件支付方式 1-账期支付 2-预付款支付）
     */
    @ApiModelProperty(value = "支付方式0全部1指定 （条件支付方式 2-账期支付 3-预付款支付）")
    private Integer payType;

    /**
     * 支付方式0全部1指定 （条件支付方式 1-账期支付 2-预付款支付）
     */
    @ApiModelProperty(value = "支付方式0全部1指定 （条件支付方式 2-账期支付 3-预付款支付）")
    private List<Integer> payTypeValues;

    /**
     * 回款方式0全部1指定（回款形式集合 1-电汇 2-银行汇票 3-银行承兑）
     */
    @ApiModelProperty(value = "回款方式0全部1指定（回款形式集合 1-电汇 2-银行汇票 3-银行承兑）")
    private Integer backAmountType;

    /**
     * 回款方式0全部1指定（回款形式集合 1-电汇 2-银行汇票 3-银行承兑）
     */
    @ApiModelProperty(value = "回款方式0全部1指定（回款形式集合 1-电汇 2-银行汇票 3-银行承兑）")
    private List<Integer> backAmountTypeValues;
}
