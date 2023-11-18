package com.yiling.admin.data.center.enterprise.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 删除标签 Form
 *
 * @author lun.yu
 * @date 2021/10/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class RemoveTagsForm extends BaseForm {

	@NotEmpty
	@ApiModelProperty(value = "标签ID集合")
	private List<Long> tagsIdList;

}
