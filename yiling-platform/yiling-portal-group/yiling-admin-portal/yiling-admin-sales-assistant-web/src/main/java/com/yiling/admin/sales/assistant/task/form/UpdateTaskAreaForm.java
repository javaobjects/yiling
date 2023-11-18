package com.yiling.admin.sales.assistant.task.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateTaskAreaForm extends BaseForm {
    @ApiModelProperty(value = "父区域code")
    private String code;
    @ApiModelProperty(value = "子区域code集合")
    private List<String> children;
}