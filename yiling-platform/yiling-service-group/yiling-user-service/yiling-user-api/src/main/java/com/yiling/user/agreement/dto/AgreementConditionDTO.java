package com.yiling.user.agreement.dto;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 协议条件VO
 * @author dexi.yao
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementConditionDTO extends BaseDTO {

	/**
	 * 专利类型 1-非专利 2-专利
	 */
	private Integer isPatent;

	/**
	 * 政策类型 1-购进额 2-回款额
	 */
	private Integer policyType;

    /**
     * 政策值
     */
    private BigDecimal policyValue;

	/**
	 * 条件规则 1-购进总额 2-月度拆解 3-季度拆解 4-梯度
	 */
	private Integer conditionRule;

	/**
	 * 条件总额
	 */
	private BigDecimal totalAmount;

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

	/**
	 * 条件序号可为月,季度,梯度
	 */
	private Integer rangeNo;

	/**
	 * 拆解百分比(仅在conditionRule！=4时该字段才有值)
	 */
	private Integer percentage;

	/**
	 * 拆解数额(仅在conditionRule！=4时该字段才有值)
	 */
	private BigDecimal amount;

	/**
	 * 梯度起始值(仅在conditionRule=4时该字段才有值)
	 */
	private BigDecimal mixValue;

	/**
	 * 梯度最大值(仅在conditionRule=4时该字段才有值)
	 */
	private BigDecimal maxValue;

	/**
	 * 时间节点
	 */
	private Integer timeNode;

    /**
     * 兑付次数
     */
	private Integer cashCount;

}
