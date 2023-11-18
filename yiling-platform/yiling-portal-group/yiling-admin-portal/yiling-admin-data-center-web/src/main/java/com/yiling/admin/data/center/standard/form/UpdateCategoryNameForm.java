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
 * @author: shuang.zhang
 * @date: 2021/5/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCategoryNameForm extends BaseForm {

    private static final long serialVersionUID = -33312528332221L;

    /**
     * id
     */
    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "分类id")
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "不能为空")
    @ApiModelProperty(value = "分类名称")
    @Length(max = 10)
    private String name;



}
