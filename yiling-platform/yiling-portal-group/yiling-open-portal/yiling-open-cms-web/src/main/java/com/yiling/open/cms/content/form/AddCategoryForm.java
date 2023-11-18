package com.yiling.open.cms.content.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

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
public class AddCategoryForm extends BaseForm {

    /**
     * 业务线id
     */
    @NotNull
    @ApiModelProperty(value = "业务线id")
    private Long lineId;

    /**
     * 模块id
     */
    @NotNull
    @ApiModelProperty(value = "模块id")
    private Long moduleId;

    /**
     * 栏目名称
     */
    @NotBlank
    @ApiModelProperty(value = "栏目名称")
    private String categoryName;

    //
    // /**
    //  * 关联业务线模块
    //  */
    // @NotNull
    // @ApiModelProperty(value = "业务线关联模块")
    // private List<CategoryLineModuleForm> lineModuleList;

}
