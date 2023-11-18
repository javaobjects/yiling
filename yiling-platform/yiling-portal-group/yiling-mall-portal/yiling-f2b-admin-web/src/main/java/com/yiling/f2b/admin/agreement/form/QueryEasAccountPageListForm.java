package com.yiling.f2b.admin.agreement.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEasAccountPageListForm extends QueryPageListForm {

	@ApiModelProperty(value = "企业名称/企业联系人")
	private String customerName;
}
