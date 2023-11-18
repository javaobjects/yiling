package com.yiling.f2b.admin.agreement.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AuditApplyForm extends BaseForm {

	@NotNull
	@ApiModelProperty(value = "申请单id")
	private Long id;

	@NotNull
	@ApiModelProperty(value = "状态 2-已入账 3-驳回")
	@Min(value = 2)
	@Max(value = 3)
	private Integer status;

	@ApiModelProperty(value = "审核原因")
	private String auditRemark;


}
