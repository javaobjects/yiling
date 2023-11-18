package com.yiling.admin.data.center.report.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/2/22
 */
@Data
@Accessors(chain = true)
public class QueryGoodsCategoryPageForm extends QueryPageListForm {

    /**
     * 子参数id
     */
    @NotNull
    @ApiModelProperty(value = "子参数id")
    private Long paramSubId;

}