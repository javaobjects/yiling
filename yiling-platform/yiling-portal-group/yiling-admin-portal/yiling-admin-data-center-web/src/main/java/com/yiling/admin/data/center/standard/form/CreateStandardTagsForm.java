package com.yiling.admin.data.center.standard.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 CreateStandardTagsForm
 * @描述
 * @创建时间 2022/10/20
 * @修改人 shichen
 * @修改时间 2022/10/20
 **/
@Data
public class CreateStandardTagsForm extends BaseForm {

    @NotEmpty
    @Length(max = 10)
    @ApiModelProperty("名称")
    private String name;

    /**
     * 描述
     */
    @Length(max = 20)
    @ApiModelProperty("描述")
    private String description;

    /**
     * 类型：1-手动标签 2-自动标签
     */
    @NotNull
    @Range(min = 1,max = 2)
    @ApiModelProperty("类型：1-手动标签 2-自动标签")
    private Integer type;
}
