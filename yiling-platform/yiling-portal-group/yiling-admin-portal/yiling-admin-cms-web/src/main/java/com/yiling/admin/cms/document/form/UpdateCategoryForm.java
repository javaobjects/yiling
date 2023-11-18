package com.yiling.admin.cms.document.form;


import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 栏目
 * </p>
 *
 * @author gxl
 * @date 2022-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "documentUpdateCategoryForm")
public class UpdateCategoryForm extends BaseForm {



    @NotNull
    private Long id;
    /**
     * 栏目名称
     */
    @ApiModelProperty(value = "栏目名称")
    private String categoryName;



    /**
     * 0-禁用 1启用
     */
    @ApiModelProperty(value = "0-禁用 1启用")
    private Integer status;
}
