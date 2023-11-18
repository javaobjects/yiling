package com.yiling.admin.erp.enterprise.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/5/31
 */
@Data
public class SelectTaskConfigVO {

    @ApiModelProperty(value = "商业公司编码", example = "1")
    private Long suId;

    @ApiModelProperty(value = "任务编码", example = "1")
    private String taskNo;

    @ApiModelProperty(value = "任务名称", example = "1")
    private String taskName;
}
