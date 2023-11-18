package com.yiling.admin.sales.assistant.commissions.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTaskPageListForm extends QueryPageListForm {

	/**
	 * 用户id
	 */
	@ApiModelProperty("用户id")
	@NotNull
	private Long userId;
}
