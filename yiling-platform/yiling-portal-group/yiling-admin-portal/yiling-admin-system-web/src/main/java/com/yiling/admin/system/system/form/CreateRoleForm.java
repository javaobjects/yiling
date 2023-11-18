package com.yiling.admin.system.system.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建角色 Form
 *
 * @author lun.yu
 * @date 2021/7/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class CreateRoleForm extends BaseForm {

	@Length(max = 50)
	@NotEmpty
	@ApiModelProperty(value = "角色名称", required = true)
	private String name;

	@Length(max = 50)
	@ApiModelProperty(value = "角色编码")
	private String code;

	@Range(min = 1,max = 2)
	@NotNull
	@ApiModelProperty(value = "角色类型：1-系统角色 2-自定义角色", required = true)
	private Integer type;

	@NotNull
	@Range(min = 1,max = 2)
	@ApiModelProperty(value = "状态：1-启用 2-停用", required = true)
	private Integer status;

	@Length(max = 50)
	@ApiModelProperty("角色描述")
	private String remark;

}
