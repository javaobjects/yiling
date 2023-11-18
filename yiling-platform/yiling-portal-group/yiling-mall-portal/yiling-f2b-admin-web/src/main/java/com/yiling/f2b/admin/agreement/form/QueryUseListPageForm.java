package com.yiling.f2b.admin.agreement.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/8/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUseListPageForm extends QueryPageListForm {

	/**
	 * 申请单编号
	 */
	@ApiModelProperty(value = "申请单编号")
	private String applicantCode;

	/**
	 * 申请企业名称
	 */
	@ApiModelProperty(value = "申请企业名称")
	private String name;

	/**
	 * 申请企业easCode
	 */
	@ApiModelProperty(value = "申请企业easCode")
	private String easCode;

}
