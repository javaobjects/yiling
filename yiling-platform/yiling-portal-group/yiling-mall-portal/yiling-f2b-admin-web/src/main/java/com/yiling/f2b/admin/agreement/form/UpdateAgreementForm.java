package com.yiling.f2b.admin.agreement.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("修改协议参数form")
public class UpdateAgreementForm extends BaseForm {

	/**
	 * 协议id
	 */
	@ApiModelProperty(value = "协议id")
	@NotNull
	private Long id;

	/**
	 * 协议名称
	 */
	@ApiModelProperty(value = "协议名称")
	private String name;

	/**
	 * 协议描述
	 */
	@ApiModelProperty(value = "协议描述")
	private String content;

	/**
	 * 原因
	 */
	@ApiModelProperty(value = "原因")
	private String remark;

	/**
	 * 协议条件
	 */
	@ApiModelProperty(value = "协议条件--仅在修改补充协议时可能需要该参数")
	private List<UpdateAgreementConditionForm> conditionFormList;



	@Data
	@ApiModel(value = "协议条件form")
	public static class UpdateAgreementConditionForm{

		/**
		 * 协议条件id
		 */
		@ApiModelProperty(value = "协议条件id")
		@NotNull
		private Long id;

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
		 * 返利百分比
		 */
		@ApiModelProperty(value ="返利百分比")
		private BigDecimal policyValue;

		/**
		 * 协议政策
		 */
		@ApiModelProperty(value ="协议政策")
		private Integer percentage;

		/**
		 * 拆解数额
		 */
		@ApiModelProperty(value ="拆解数额")
		private BigDecimal amount;

		/**
		 * 时间节点
		 */
		@ApiModelProperty(value ="时间节点")
		private Integer timeNode;

		/**
		 * 梯度天数起始值
		 */
		@ApiModelProperty(value ="梯度天数起始值")
		private Integer mixValue;

		/**
		 * 梯度天数最大值
		 */
		@ApiModelProperty(value ="梯度天数最大值")
		private Integer maxValue;
	}
}
