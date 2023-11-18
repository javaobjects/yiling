package com.yiling.admin.data.center.enterprise.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryReceiptAccountForm extends BaseForm {

	@ApiModelProperty("企业收款账户id")
	@NotNull()
	private Long  receiptAccountId;
}
