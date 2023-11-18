package com.yiling.f2b.admin.procrelation.form;

import javax.validation.constraints.NotBlank;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023/6/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveProcTemplateForm extends BaseForm {

    /**
     * id
     */
    @ApiModelProperty(value = "id---更新是必传")
    private Long id;

    /**
     * 模板名称
     */
    @NotBlank
    @ApiModelProperty(value = "模板名称")
    private String templateName;
}
