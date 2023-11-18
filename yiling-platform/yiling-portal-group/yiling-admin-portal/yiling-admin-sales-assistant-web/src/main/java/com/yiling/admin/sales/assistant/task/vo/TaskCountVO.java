package com.yiling.admin.sales.assistant.task.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务数量VO
 * @author: hongyang.zhang
 * @data: 2021/09/13
 */
@Data
public class TaskCountVO {

    @ApiModelProperty(value = "任务总数")
    private Long count;

    @ApiModelProperty(value = "平台任务数")
    private Long platformCount;

    @ApiModelProperty(value = "企业任务数")
    private Long enterpriseCount;
}
