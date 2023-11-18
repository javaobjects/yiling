package com.yiling.sales.assistant.app.mr.document.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文献分页列表查询参数
 * @author: gxl
 * @date: 2022/3/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryDocumentPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "栏目id")
    private Long categoryId;

    @ApiModelProperty(value = "文献标题")
    private String title;
}