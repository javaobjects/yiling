package com.yiling.sales.assistant.app.task.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2021/09/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryLockTerminalListForm extends QueryPageListForm {

    @NotNull
    @ApiModelProperty(value = "用户任务id", required = true)
    private Long userTaskId;

}
