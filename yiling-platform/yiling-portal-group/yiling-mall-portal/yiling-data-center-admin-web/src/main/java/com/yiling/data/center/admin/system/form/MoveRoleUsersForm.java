package com.yiling.data.center.admin.system.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 转移角色人员 Form
 * 
 * @author xuan.zhou
 * @date 2021/7/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MoveRoleUsersForm extends BaseForm {

	@NotNull
	@ApiModelProperty(value = "角色ID", required = true)
	private Long id;

	@NotNull
	@ApiModelProperty(value = "目标角色ID", required = true)
	private Long newId;
}
