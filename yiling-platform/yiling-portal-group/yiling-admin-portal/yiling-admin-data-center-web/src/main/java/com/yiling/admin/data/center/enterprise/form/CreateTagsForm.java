package com.yiling.admin.data.center.enterprise.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 创建标签 Form
 *
 * @author: lun.yu
 * @date: 2021/10/27
 */
@Data
public class CreateTagsForm extends BaseForm {

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
