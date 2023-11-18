package com.yiling.admin.sales.assistant.task.vo;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 任务追踪返回实体
 * @author: ray
 * @date: 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TaskTraceVO extends BaseDTO {
    @ApiModelProperty(value = "承接人数")
    private Integer takeCount;
    @ApiModelProperty(value = "已完成人数")
    private Integer finishCount;
    @ApiModelProperty(value = "拉人总数")
    private Integer userCount;
    @ApiModelProperty(value = "拉户总数")
    private Integer enterpriseCount;

    @ApiModelProperty(value = "成功推广次数")
    private Integer memberBuyCount;
}