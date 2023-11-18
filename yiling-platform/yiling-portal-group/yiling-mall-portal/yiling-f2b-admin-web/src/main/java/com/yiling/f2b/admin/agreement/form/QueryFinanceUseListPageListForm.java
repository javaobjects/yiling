package com.yiling.f2b.admin.agreement.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFinanceUseListPageListForm extends QueryPageListForm {

	@ApiModelProperty(value = "eid")
	@NotNull
	private Long eid;

	@ApiModelProperty(value = "easCode")
	@NotEmpty
	private String easCode;
}
