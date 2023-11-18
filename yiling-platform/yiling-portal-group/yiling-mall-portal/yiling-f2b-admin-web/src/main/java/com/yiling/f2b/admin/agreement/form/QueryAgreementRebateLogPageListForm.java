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
 * @date 2021-07-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementRebateLogPageListForm extends QueryPageListForm {

	/**
	 * eas账号
	 */
	@NotEmpty
	@ApiModelProperty(value = "eas账号")
	private String easAccount;

	/**
	 * eid
	 */
	@NotNull
	@ApiModelProperty(value = "eas账号")
	private Long eid;

}
