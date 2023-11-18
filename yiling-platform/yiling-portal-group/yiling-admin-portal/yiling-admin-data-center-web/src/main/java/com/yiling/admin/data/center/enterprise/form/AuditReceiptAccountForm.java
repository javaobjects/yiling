package com.yiling.admin.data.center.enterprise.form;

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
 * @date 2021-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AuditReceiptAccountForm extends BaseForm {

	@ApiModelProperty("企业收款账户id")
	@NotNull()
	private Long  id;

	/**
	 * 账户状态 1-待审核 2-审核成功 3-审核失败
	 */
	@ApiModelProperty("账户状态")
	@Min(value = 2)
	@Max(value = 3)
	private Integer  status;

	/**
	 * 审核描述
	 */
	@ApiModelProperty("审核描述")
	private String auditRemark;
}
