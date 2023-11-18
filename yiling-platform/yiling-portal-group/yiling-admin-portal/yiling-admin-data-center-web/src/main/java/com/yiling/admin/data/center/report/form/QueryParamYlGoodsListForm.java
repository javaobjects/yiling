package com.yiling.admin.data.center.report.form;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/6/2
 */
@Data
public class QueryParamYlGoodsListForm extends BaseForm {

    @NotEmpty
    @ApiModelProperty(value = "以岭品名称")
    private String ylGoodsName;

}
