package com.yiling.user.agreement.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModel;
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
public class UpdateAgreementRequest extends BaseRequest {

	private static final long serialVersionUID = 4589992723878573265L;
	/**
	 * 协议id
	 */
	@NotNull
	private Long id;

	/**
	 * 协议分类：1-主协议 2-临时协议
	 */
	@NotNull
	private Integer category;


	/**
	 * 协议名称
	 */
	private String name;

	/**
	 * 协议描述
	 */
	private String content;

	/**
	 * 原因
	 */
	private String remark;

	/**
	 * 协议条件--仅在修改补充协议时可能需要该参数
	 */
	private List<UpdateAgreementConditionRequest> conditionFormList;


	/**
	 * 协议条件form
	 */
	@Data
	public static class UpdateAgreementConditionRequest implements Serializable {

		/**
		 * 条件id
		 */
		@NotNull
		private Long id;

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
	}
}
