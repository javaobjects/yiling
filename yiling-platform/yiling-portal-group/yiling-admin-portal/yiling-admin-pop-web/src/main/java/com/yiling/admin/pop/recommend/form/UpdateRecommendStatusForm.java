package com.yiling.admin.pop.recommend.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 编辑banner Form
 * </p>
 *
 * @author wei.wang
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateRecommendStatusForm extends BaseForm {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "recommendId")
    @NotNull(message = "id不能为空")
    private Long id;


    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    @NotNull(message = "状态不能为空")
    private Integer status;

}
