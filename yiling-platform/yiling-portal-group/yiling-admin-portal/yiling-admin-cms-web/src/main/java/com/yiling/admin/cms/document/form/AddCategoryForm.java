package com.yiling.admin.cms.document.form;

import javax.validation.constraints.NotEmpty;

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
@ApiModel(value = "documentAddCategoryForm")
public class AddCategoryForm extends BaseForm {


    /**
     * 栏目名称
     */
    @NotEmpty
    @ApiModelProperty(value = "栏目名称")
    private String categoryName;

}
