package com.yiling.sjms.workflow.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 可驳回节点
 * @author: gxl
 * @date: 2022/11/30
 */
@Data
public class WorkFlowNodeVO {

    @ApiModelProperty(value = "节点id")
    private String nodeId;

    @ApiModelProperty(value = "节点名称")
    private String nodeName;


}