package com.yiling.sales.assistant.app.task.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * app任务列表查询参数实体
 * </p>
 *
 * @author gxl
 * @since 2020-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTaskPageForm extends QueryPageListForm {


    @NotNull
    @ApiModelProperty(value = "任务主体 0:平台任务1：企业任务")
    private Integer taskType;

    @ApiModelProperty(value = "不需要app传,网关带过来")
    private String userId;

    @ApiModelProperty(value = "企业id")
    private Integer eid;
}
