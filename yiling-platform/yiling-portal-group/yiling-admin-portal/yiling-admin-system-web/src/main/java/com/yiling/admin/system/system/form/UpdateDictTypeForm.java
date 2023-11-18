package com.yiling.admin.system.system.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class UpdateDictTypeForm extends BaseForm {
    /**
     * 字典内容id
     */
    @NotNull(message = "不能为空")
    @ApiModelProperty("id")
    private Long id;
    /**
     * 字典名称
     */
    @NotBlank(message = "不能为空")
    @ApiModelProperty("字典名称")
    private String name;

    /**
     * 字典描述
     */
    @ApiModelProperty("字典描述")
    private String description;
}
