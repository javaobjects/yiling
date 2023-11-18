package com.yiling.open.web.form;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/7/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddRebateUseForm extends BaseForm {


	/**
	 * 申请单id
	 */
	@ApiModelProperty(value = "申请单id")
	@NotNull
	private String applicantId;

	/**
	 * 申请单编号
	 */
	@ApiModelProperty(value = "申请单编号")
	@NotNull
	private String applicantCode;

	/**
	 * 申请企业easCode
	 */
	@ApiModelProperty(value = "申请企业easCode")
	@NotNull
	private String easCode;

	/**
	 * 申请企业名称
	 */
	@ApiModelProperty(value = "申请企业名称")
	@NotNull
	private String name;

	/**
	 * 销售组织easCode
	 */
	@ApiModelProperty(value = "销售组织easCode")
	@NotNull
	private String sellerCode;

	/**
	 * 销售组织名称
	 */
	@ApiModelProperty(value = "销售组织名称")
	@NotNull
	private String sellerName;

	/**
	 * 省份
	 */
	@ApiModelProperty(value = "省份")
	@NotNull
	private String provinceName;

	/**
	 * 申请总金额
	 */
	@ApiModelProperty(value = "申请总金额")
	@NotNull
	private BigDecimal totalAmount;

	/**
	 * 执行方式1- 票折 2- 现金 3-冲红 4-健康城卡
	 */
	@ApiModelProperty(value = "执行方式(1-票折 2-现金 3-冲红 4-健康城卡)")
	@NotNull
	@Max(value = 4)
	@Min(value = 1)
	private Integer executeMeans;

	/**
	 * 申请单状态1-草稿 2-为提交 3-审核成功 4-驳回 -5撤回
	 */
	@ApiModelProperty(value = "申请单状态 1-草稿 2-提交")
	@Max(value = 2)
	@Min(value = 1)
	@NotNull
	private Integer status;

	/**
	 * 返利类型
	 */
	@ApiModelProperty(value = "返利类型",example = "商务返利")
	@NotNull
	private String rebateCategory;

	/**
	 * 执行部门
	 */
	@ApiModelProperty(value = "执行部门",example = "商务部")
	@NotNull
	private String executeDept;

	/**
	 * 申请时间
	 */
	@ApiModelProperty(value = "申请时间")
	@NotNull
	private Date createTime;

	/**
	 * 申请人
	 */
	@ApiModelProperty(value = "申请人")
	private Long createUser;

	/**
	 * 申请人名称
	 */
	@ApiModelProperty(value = "申请人名称")
	@NotNull
	private String createUserName;

	/**
	 * 申请人工号
	 */
	@ApiModelProperty(value = "申请人工号")
	@NotNull
	private String createUserCode;

	@ApiModelProperty(value = "返利申请明细")
	@NotNull
	private List<RebateDetail> rebateDetail;

	@ApiModelProperty(value = "返利申请明细")
	private List<RebateGoods> goodsDetail;

	@Data
	public static class RebateDetail {

		/**
		 * 所属年度
		 */
		@ApiModelProperty(value = "所属年度")
		@NotNull
		private String ascriptionYearValue;

		/**
		 * 所属月度（1-12）、季度（1-4）、其余该值为 0
		 */
		@ApiModelProperty(value = "所属月度")
		@NotNull
		private String cycleValue;

		/**
		 * 返利金额
		 */
		@ApiModelProperty(value = "返利金额")
		@NotNull
		private BigDecimal amount;

		/**
		 * 返利种类
		 */
		@ApiModelProperty(value = "返利种类")
		@NotNull
		private String rebateCategory;

		/**
		 * 费用科目
		 */
		@ApiModelProperty(value = "费用科目")
		@NotNull
		private String costSubject;

		/**
		 * 费用归属部门
		 */
		@ApiModelProperty(value = "费用归属部门")
		@NotNull
		private String costDept;

		/**
		 * 执行部门
		 */
		@ApiModelProperty(value = "执行部门")
		@NotNull
		private String executeDept;

		/**
		 * 批复代码
		 */
		@ApiModelProperty(value = "批复代码")
		@NotNull
		private String replyCode;
	}

	@Data
	public static class RebateGoods {

		/**
		 * 商品编码
		 */
		@ApiModelProperty(value = "商品编码")
		private String goodsCode;

		/**
		 * 商品名称
		 */
		@ApiModelProperty(value = "商品名称")
		private String goodsName;

		/**
		 * 规格
		 */
		@ApiModelProperty(value = "规格")
		private String goodsSpec;

		/**
		 * 无税金额
		 */
		@ApiModelProperty(value = "无税金额")
		private BigDecimal freeAmount;

		/**
		 * 税率
		 */
		@ApiModelProperty(value = "税率")
		private BigDecimal taxRate;

		/**
		 * 税额
		 */
		@ApiModelProperty(value = "税额")
		private BigDecimal taxAmount;

		/**
		 * 含税金额
		 */
		@ApiModelProperty(value = "含税金额")
		private BigDecimal amountIncludingTax;
	}

}
