package com.yiling.f2b.admin.agreement.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveRebateApplyForm extends BaseForm {

	/**
	 * 企业id
	 */
	@NotNull
	@ApiModelProperty(value = "企业id")
	private Long eid;

	/**
	 * 企业编码
	 */
	@NotNull
	@ApiModelProperty(value = "企业编码")
	private String easCode;


	/**
	 * 年度
	 */
	@NotNull
	@ApiModelProperty(value = "年度")
	private Integer year;

	/**
	 * 月度（1~12月 13~16-季度 17-上半年 18-下半年 19-全年）
	 */
	@NotNull
	@ApiModelProperty(value = "月度（1~12月 13~16-季度 17-上半年 18-下半年 19-全年）")
	@Min(value = 1)
	@Max(value = 19)
	private Integer month;

	/**
	 * 入账企业id
	 */
	@ApiModelProperty(value = "一级商id")
	private Long entryEid;


	/**
	 * 入账企业easCode
	 */
	@ApiModelProperty(value = "一级商code")
	private String entryCode;

	/**
	 * 省份id
	 */
	@ApiModelProperty(value = "省份")
	@NotEmpty
	private String provinceName;


	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	@NotEmpty
	private String goodsName;

	/**
	 * 返利总额
	 */
	@ApiModelProperty(value = "返利总额")
	@NotNull
	private BigDecimal totalAmount;

	/**
	 * 是否点击计算按钮
	 */
	@NotNull
	@ApiModelProperty(value = "是否点击计算按钮：0-否 1-是")
	private Integer calculateStatus;

	@ApiModelProperty("其他返利列表")
	private List<RebateApplyDetail> applyDetails;

	@Data
	public static class RebateApplyDetail{

		/**
		 * 金额
		 */
		@ApiModelProperty(value = "金额")
		@NotNull
		private BigDecimal amount;

		/**
		 * 入账原因
		 */
		@ApiModelProperty(value = "入账原因")
		@NotNull
		private String entryDescribe;

		/**
		 * 销售组织id
		 */
		@ApiModelProperty(value = "销售组织id")
		@NotNull
		private Long sellerEid;

		/**
		 * 销售组织名称
		 */
		@ApiModelProperty(value = "销售组织名称")
		@NotNull
		private String sellerName;


	}


}
