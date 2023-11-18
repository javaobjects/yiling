package com.yiling.f2b.admin.agreement.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EditApplyDetailForm extends BaseForm {

	/**
	 * 申请单id
	 */
	@ApiModelProperty(value = "申请单id")
	@NotNull
	private Long applyId;

	/**
	 * 明细id
	 */
	@ApiModelProperty(value = "明细id")
	@NotNull
	private Long id;

	/**
	 * 返利金额
	 */
	@ApiModelProperty(value = "返利金额")
	@NotNull
	private BigDecimal amount;

	/**
	 * 入账原因
	 */
	@ApiModelProperty(value = "入账原因")
	private String entryDescribe;


}
