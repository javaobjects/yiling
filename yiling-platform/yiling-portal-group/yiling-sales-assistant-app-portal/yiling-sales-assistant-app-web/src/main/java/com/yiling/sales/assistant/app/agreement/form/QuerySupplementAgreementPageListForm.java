package com.yiling.sales.assistant.app.agreement.form;

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
public class QuerySupplementAgreementPageListForm extends QueryPageListForm {

	@NotNull
	@ApiModelProperty(value = "年度协议Id")
	private Long   agreementId;

	@NotNull
	@ApiModelProperty(value = "协议状态：1-进行中 2-未开始 3-已停用 4-已过期")
	private Integer   agreementStatus;
}
