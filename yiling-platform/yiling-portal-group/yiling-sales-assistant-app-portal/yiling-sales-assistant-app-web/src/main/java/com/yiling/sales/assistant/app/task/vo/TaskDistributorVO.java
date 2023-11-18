package com.yiling.sales.assistant.app.task.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务配送商
 *
 * @author: ray
 * @date: 2021/10/19
 */
@Data
public class TaskDistributorVO extends BaseVO {
    @ApiModelProperty(value = "配送商名称")
    private String name;

}