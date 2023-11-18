package com.yiling.admin.data.center.article.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 切换文章状态 Form
 *
 * @author: fan.shen
 * @date: 2021/12/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SwitchStatusForm extends BaseForm {

    /**
     * 文章ID
     */
    @ApiModelProperty("文章ID")
    @NotNull(message = "文章id不能为空")
    private Long    id;

    /**
     * 文章状态
     */
    @ApiModelProperty("文章状态")
    @NotNull(message = "文章状态不能为空")
    @Min(value = 1)
    @Max(value = 2)
    private Integer articleStatus;

}
