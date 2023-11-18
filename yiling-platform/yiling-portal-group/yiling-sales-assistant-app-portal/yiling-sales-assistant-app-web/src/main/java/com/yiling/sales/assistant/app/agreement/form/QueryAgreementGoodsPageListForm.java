package com.yiling.sales.assistant.app.agreement.form;


import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:dexi.yao
 * @date:2021/09/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementGoodsPageListForm extends QueryPageListForm {

	/**
	 * 协议id
	 */
	@ApiModelProperty(value = "协议主键id")
	@NotNull(message = "协议主键不能为空")
	private Long agreementId;
}
