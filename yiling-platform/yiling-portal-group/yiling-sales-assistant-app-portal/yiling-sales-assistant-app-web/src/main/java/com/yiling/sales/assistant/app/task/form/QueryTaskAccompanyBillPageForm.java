package com.yiling.sales.assistant.app.task.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTaskAccompanyBillPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "用户任务id",required = true)
    private Long userTaskId;
}