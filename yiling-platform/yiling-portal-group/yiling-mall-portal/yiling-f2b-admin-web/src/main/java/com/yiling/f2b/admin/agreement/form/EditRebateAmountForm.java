package com.yiling.f2b.admin.agreement.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EditRebateAmountForm extends BaseForm {

	/**
	 * account
	 */
	@NotEmpty
	@ApiModelProperty(value = "企业账号")
	private String account;

	/**
	 * reason
	 */
	@NotEmpty
	@ApiModelProperty(value = "原因")
	private String logName;

	/**
	 * 金额
	 */
	@NotNull
	@ApiModelProperty(value = "金额")
	private BigDecimal discountAmount;
}
