package com.yiling.sales.assistant.app.rebate.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryApplyPageListForm extends QueryPageListForm {

	/**
	 * 返利所属企业id
	 */
	@ApiModelProperty(value = "返利所属企业id")
	@NotNull
	private Long eid;

	/**
	 * 申请单号
	 */
	@ApiModelProperty(value = "申请单号")
	private String code;

	/**
	 * 状态 1-待审核 2-已入账 3-驳回
	 */
	@ApiModelProperty(value = "状态 1-待审核 2-已入账 3-驳回")
	private Integer status;

	/**
	 * 所属年度
	 */
	@ApiModelProperty(value = "所属年度（不能为0）")
	private Integer year;

	/**
	 * 所属月度
	 */
	@ApiModelProperty(value = "所属月度（不能为0），传入此参数时年度为必填项")
	private Integer month;

	/**
	 * 申请时间-开始
	 */
	@ApiModelProperty(value = "申请时间-开始")
	private Date startDate;

	/**
	 * 申请时间-结束
	 */
	@ApiModelProperty(value = "申请时间-结束")
	private Date endDate;

	/**
	 * 顺序类型 1-正序 2-倒序
	 */
	@ApiModelProperty(value = "顺序类型 1-正序 2-倒序")
	private Integer sequence;

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
