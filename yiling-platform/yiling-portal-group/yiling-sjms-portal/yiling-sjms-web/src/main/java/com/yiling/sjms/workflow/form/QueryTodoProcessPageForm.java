package com.yiling.sjms.workflow.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询代办列表入参
 * @author: gxl
 * @date: 2022/11/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTodoProcessPageForm extends QueryPageListForm {
    @ApiModelProperty(value = "编号")
    private String gbNo;

    @ApiModelProperty(value = "所属流程 字典process_category")
    private String category;


}