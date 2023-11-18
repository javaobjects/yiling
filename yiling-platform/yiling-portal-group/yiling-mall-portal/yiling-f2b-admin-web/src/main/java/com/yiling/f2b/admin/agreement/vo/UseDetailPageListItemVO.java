package com.yiling.f2b.admin.agreement.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UseDetailPageListItemVO extends BaseDTO {

	/**
	 * 所属年度
	 */
	@ApiModelProperty(value = "所属年度")
	private String year;

	/**
	 * 所属月度
	 */
	@ApiModelProperty(value = "所属月度")
	private String month;

	/**
	 * 返利金额
	 */
	@ApiModelProperty(value = "返利金额")
	private BigDecimal amount;

	/**
	 * 返利种类
	 */
	@ApiModelProperty(value = "返利种类")
	private String rebateCategory;

	/**
	 * 费用科目
	 */
	@ApiModelProperty(value = "费用科目")
	private String costSubject;

	/**
	 * 费用归属部门
	 */
	@ApiModelProperty(value = "费用归属部门")
	private String costDept;

	/**
	 * 批复代码
	 */
	@ApiModelProperty(value = "批复代码")
	private String replyCode;

}
