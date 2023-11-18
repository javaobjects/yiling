package com.yiling.admin.system.system.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建角色关联菜单 Form
 *
 * @author lun.yu
 * @date 2021/7/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class RoleMenuForm extends BaseForm {

	@NotNull
	@ApiModelProperty(value = "角色id",required = true)
	private Long roleId;

	@NotNull
	@ApiModelProperty(value = "角色对应菜单集合", required = true)
	private List<Long> menuIdList;

}
