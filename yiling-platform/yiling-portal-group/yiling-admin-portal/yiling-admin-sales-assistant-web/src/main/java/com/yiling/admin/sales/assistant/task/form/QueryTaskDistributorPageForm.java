package com.yiling.admin.sales.assistant.task.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 编辑任务反显
 * @author: ray
 * @date: 2021/10/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryTaskDistributorPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "类型：1-云仓 2-非云仓 0-全部")
    private Integer type;

    @ApiModelProperty(value = "任务id",required = true)
    private Long taskId;
}