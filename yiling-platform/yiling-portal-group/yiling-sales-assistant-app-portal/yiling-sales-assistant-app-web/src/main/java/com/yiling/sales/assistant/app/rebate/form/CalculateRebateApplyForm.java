package com.yiling.sales.assistant.app.rebate.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 计算协议返利申请form
 *
 * @author dexi.yao
 * @date 2021-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CalculateRebateApplyForm extends BaseForm {

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
	 * 月的类型常量
	 */
	@ApiModelProperty(hidden = true)
	public static Integer monthType12 = 12;

	/**
	 * 月的类型常量
	 */
	@ApiModelProperty(hidden = true)
	public static Integer monthType13 = 13;
	/**
	 * 月的类型常量
	 */
	@ApiModelProperty(hidden = true)
	public static Integer monthType14 = 14;
	/**
	 * 月的类型常量
	 */
	@ApiModelProperty(hidden = true)
	public static Integer monthType15 = 15;

	/**
	 * 月的类型常量
	 */
	@ApiModelProperty(hidden = true)
	public static Integer monthType16 = 16;

	/**
	 * 月的类型常量
	 */
	@ApiModelProperty(hidden = true)
	public static Integer monthType17 = 17;

	/**
	 * 月的类型常量
	 */
	@ApiModelProperty(hidden = true)
	public static Integer monthType18 = 18;

	/**
	 * 月的类型常量
	 */
	@ApiModelProperty(hidden = true)
	public static Integer monthType19 = 19;

}
