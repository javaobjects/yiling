package com.yiling.open.web.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/7/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class WithdrawRebateApplyForm extends BaseForm {

	/**
	 * 申请单id
	 */
	@ApiModelProperty(value = "申请单id")
	@NotNull
	private String applicantId;

	/**
	 * 撤回原因
	 */
	@ApiModelProperty(value = "撤回原因")
	private String withdrawRemark;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "操作人Id")
	private Long updateUser;

	/**
	 * 更新人名称
	 */
	@ApiModelProperty(value = "操作人名称")
	@NotNull
	private String updateUserName;

	/**
	 * 更新人工号
	 */
	@ApiModelProperty(value = "操作人工号")
	@NotNull
	private String updateUserCode;

}
