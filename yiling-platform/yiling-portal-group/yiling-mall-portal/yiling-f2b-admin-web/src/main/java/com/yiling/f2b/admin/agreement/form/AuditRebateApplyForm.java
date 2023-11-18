package com.yiling.f2b.admin.agreement.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AuditRebateApplyForm extends BaseForm {

	/**
	 * 协议申请id
	 */
	@NotNull
	@ApiModelProperty(value = "协议申请id")
	private Long applyId;

	/**
	 * 状态  2-已入账 3-驳回
	 */
	@NotNull
	@ApiModelProperty(value = "2-成功 3-驳回")
	private Integer status;

	/**
	 * 审核原因
	 */
	@ApiModelProperty(value = "审核原因")
	private String auditRemark;
}
