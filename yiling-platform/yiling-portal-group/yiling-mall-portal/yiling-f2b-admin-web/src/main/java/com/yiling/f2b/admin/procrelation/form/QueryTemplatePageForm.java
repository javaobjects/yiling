package com.yiling.f2b.admin.procrelation.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023/6/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTemplatePageForm extends QueryPageListForm {

    /**
     * 模板编号
     */
    @ApiModelProperty("模板编号")
    private String templateNumber;

    /**
     * 模板名称
     */
    @ApiModelProperty("模板名称")
    private String templateName;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String operationUser;
}
