package com.yiling.admin.data.center.enterprise.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 更新标签 Form
 *
 * @author: lun.yu
 * @date: 2021/10/27
 */
@Data
public class UpdateTagsForm extends CreateTagsForm{

    @NotNull
    @Min(1)
    @ApiModelProperty("ID")
    private Long id;

}
