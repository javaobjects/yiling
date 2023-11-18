package com.yiling.f2b.admin.agreement.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryThirdAgreementCountForm extends BaseForm {

	@NotNull
	@ApiModelProperty(value = "当前查询企业eid")
	private Long   queryEid;

	/**
	 * 丙方eid
	 */
	@NotNull
	@ApiModelProperty(value = "企业列表中的eid")
	private Long  eid;

}
