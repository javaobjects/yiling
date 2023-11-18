package com.yiling.admin.sales.assistant.task.vo;

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

    private Integer type;

    @ApiModelProperty(value = "配送商企业id")
    private Long distributorEid;
}