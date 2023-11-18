package com.yiling.admin.system.system.form;

import javax.validation.constraints.NotBlank;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class SaveDictTypeForm extends BaseForm {

    /**
     * 字典名称
     */
    @NotBlank(message = "不能为空")
    @ApiModelProperty("字典名称")
    private String name;

    /**
     * 字典描述
     */
    @NotBlank(message = "不能为空")
    @ApiModelProperty("字典描述")
    private String description;
}
