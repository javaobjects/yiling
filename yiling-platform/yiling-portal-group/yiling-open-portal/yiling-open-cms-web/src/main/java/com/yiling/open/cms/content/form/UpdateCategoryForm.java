package com.yiling.open.cms.content.form;


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
public class UpdateCategoryForm extends BaseForm {

    @NotNull
    private Long id;
    /**
     * 栏目名称
     */
    @ApiModelProperty(value = "栏目名称")
    private String categoryName;
    //
    // /**
    //  * 引用业务线
    //  */
    // @ApiModelProperty(value = "引用业务线")
    // private List<Long> displayLines;

    /**
     * 0-禁用 1启用
     */
    @ApiModelProperty(value = "0-禁用 1启用")
    private Integer status;

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

    @ApiModelProperty(value = "排序")
    private Integer categorySort;


}
