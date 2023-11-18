package com.yiling.sales.assistant.app.task.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询我的任务列表入参
 * @author: ray
 * @date: 2021/9/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMyTaskForm extends QueryPageListForm {
    @NotNull
    @ApiModelProperty(value = "任务主体 0:平台任务1：企业任务")
    private Integer taskType;
}