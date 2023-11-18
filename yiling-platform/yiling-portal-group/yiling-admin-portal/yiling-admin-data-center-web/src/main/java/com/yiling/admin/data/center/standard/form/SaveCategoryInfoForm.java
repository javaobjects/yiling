package com.yiling.admin.data.center.standard.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCategoryInfoForm extends BaseForm {

    private static final long serialVersionUID = -3331124112221L;

    /**
     * 父类ID
     */
    @NotNull(message = "父类ID不能为空")
    @ApiModelProperty(value = "父类ID")
    private Long parentId;

    /**
     * 分类名称
     */
    @NotBlank(message = "类别名称不能为空")
    @ApiModelProperty(value = "类别名称")
    @Length(max = 10)
    private String name;

}
