package com.yiling.sales.assistant.app.agreement.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEntPageListForm extends QueryPageListForm {

	/**
	 * 查询名称
	 */
	@ApiModelProperty(value = "查询名称")
	private String queryStr;
}
