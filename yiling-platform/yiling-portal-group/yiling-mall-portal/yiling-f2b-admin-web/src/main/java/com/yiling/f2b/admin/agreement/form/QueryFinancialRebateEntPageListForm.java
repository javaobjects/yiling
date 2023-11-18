package com.yiling.f2b.admin.agreement.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFinancialRebateEntPageListForm extends QueryPageListForm {

	/**
	 * 企业名称（全模糊查询）
	 */
	@ApiModelProperty(value = "企业名称（全模糊查询）")
	private String customerName;

	/**
	 * 企业编码
	 */
	@ApiModelProperty(value = "企业编码")
	private String easCode;

	/**
	 * 执业许可证号/社会信用统一代码
	 */
	@ApiModelProperty(value = "执业许可证号/社会信用统一代码")
	private String licenseNumber;
}
