package com.yiling.admin.system.system.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 删除角色 Form
 *
 * @author lun.yu
 * @date 2021/7/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class RemoveRoleForm extends BaseForm {

	@ApiModelProperty(value = "角色ID集合")
	private List<Long> roleIdList;

}
