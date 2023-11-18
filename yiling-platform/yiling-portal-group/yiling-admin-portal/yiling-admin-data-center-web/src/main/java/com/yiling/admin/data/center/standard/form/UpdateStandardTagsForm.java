package com.yiling.admin.data.center.standard.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 UpdateStandardTagsForm
 * @描述
 * @创建时间 2022/10/20
 * @修改人 shichen
 * @修改时间 2022/10/20
 **/
@Data
public class UpdateStandardTagsForm extends CreateStandardTagsForm{
    @NotNull
    @Min(1)
    @ApiModelProperty("ID")
    private Long id;
}
