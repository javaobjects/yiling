package com.yiling.sales.assistant.app.agreement.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-14
 */
@Data
@Accessors(chain = true)
@ApiModel("协议条件VO")
public class AgreementConditionVO {

	/**
	 * 专利类型 1-非专利 2-专利
	 */
	@ApiModelProperty(value = "专利类型 1-非专利 2-专利")
	private Integer isPatent;


	/**
	 * 条件规则 1-购进总额 2-月度拆解 3-季度拆解 4-梯度
	 */
	@ApiModelProperty(value = "条件规则 1-购进总额 2-月度拆解 3-季度拆解 4-梯度")
	private Integer conditionRule;

	/**
	 * 条件总额
	 */
	@ApiModelProperty(value = "条件总额")
	private BigDecimal totalAmount;

	/**
	 * 支付方式（条件支付方式 1-账期支付 2-预付款支付）
	 */
	@ApiModelProperty(value = "支付方式（条件支付方式 1-账期支付 2-预付款支付）")
	private Integer payType;

	/**
	 * 回款方式（回款形式集合 1-电汇 2-银行汇票 3-银行承兑）
	 */
	@ApiModelProperty(value = "回款方式（回款形式集合 1-电汇 2-银行汇票 3-银行承兑）")
	private Integer backAmountType;

    /**
     * 支付方式（条件支付方式 1-账期支付 2-预付款支付）
     */
    @ApiModelProperty(value = "支付方式（条件支付方式 1-账期支付 2-预付款支付）")
    private List<Integer> payTypeValues;

	/**
	 * 回款方式（回款形式集合 1-电汇 2-银行汇票 3-银行承兑）
	 */
	@ApiModelProperty(value = "回款方式（回款形式集合 1-电汇 2-银行汇票 3-银行承兑）")
	private List<Integer> backAmountTypeValues;

	/**
	 * 条件规则列表详情
	 */
	@ApiModelProperty(value = "条件规则列表详情")
	private List<ConditionDetailVO> conditions;

	@Data
	public static class ConditionDetailVO{

		/**
		 * 协议条件id
		 */
		@ApiModelProperty(value = "协议条件id")
		private Long id;

		/**
		 * 条件序号可为月,季度,梯度
		 */
		@ApiModelProperty(value = "条件序号可为月,季度,梯度")
		private Integer rangeNo;

		/**
		 * 拆解百分比
		 */
		@ApiModelProperty(value = "拆解百分比(仅在conditionRule！=4时该字段才有值)")
		private Integer percentage;

		/**
		 * 拆解数额(仅在conditionRule！=4时该字段才有值)
		 */
		@ApiModelProperty(value = "拆解数额(仅在conditionRule！=4时该字段才有值)")
		private BigDecimal amount;

		/**
		 * 梯度起始值(仅在conditionRule=4时该字段才有值)
		 */
		@ApiModelProperty(value = "梯度起始值(仅在conditionRule=4时该字段才有值)")
		private BigDecimal mixValue;

		/**
		 * 梯度最大值(仅在conditionRule=4时该字段才有值)
		 */
		@ApiModelProperty(value = "梯度最大值(仅在conditionRule=4时该字段才有值)")
		private BigDecimal maxValue;

		/**
		 * 固定时间节点
		 */
		@ApiModelProperty(value = "固定时间节点(仅在conditionRule=5时该字段才有值)")
		private Integer timeNode;
	}
}
