package com.yiling.f2b.admin.agreement.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

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
public class QueryYearAgreementPageListForm extends QueryPageListForm {

	@NotNull
	@ApiModelProperty(value = "当前查询企业eid")
	private Long   queryEid;

	@NotNull
	@ApiModelProperty(value = "协议状态：0- 全部 1-进行中 2-未开始 3-已停用 4-已过期 5-进行中&未开始")
	private Integer   agreementStatus;
}
