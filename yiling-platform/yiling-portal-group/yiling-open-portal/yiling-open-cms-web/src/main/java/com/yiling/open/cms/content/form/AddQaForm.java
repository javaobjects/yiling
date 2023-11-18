package com.yiling.open.cms.content.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加QA form
 *
 * @author: fan.shen
 * @date: 2023/3/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddQaForm extends BaseForm {

    @ApiModelProperty("contentId")
    @NotNull
    private Long contentId;

    @ApiModelProperty("qaId")
    @NotNull
    private Long qaId;

    @ApiModelProperty("doctorId")
    @NotNull
    private Long doctorId;

    @ApiModelProperty(value = "内容")
    @NotBlank
    private String content;

}