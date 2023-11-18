package com.yiling.sjms.workflow.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gxl
 */
@Data
public class WfTaskVO {
    /**
     * 任务编号
     */
    @ApiModelProperty(value = "任务id")
    private String taskId;

    /**
     * 团购ID
     */
    @ApiModelProperty(value = "团购ID")
    private Long gbId;
    /**
     * 任务执行人Id
     */
    private String assigneeId;

    /**
     * 流程发起人部门名称
     */
    @ApiModelProperty(value = "发起人部门名称")
    private String startDeptName;

    /**
     * 流程发起人名称
     */
    @ApiModelProperty(value = "发起人")
    private String startUserName;

    /**
     * 流程定义名称
     */
    @ApiModelProperty(value = "所属流程")
    private String procDefName;

    /**
     * 流程实例ID
     */
    @ApiModelProperty(value = "流程实例Id")
    private String procInsId;


    /**
     * 任务创建时间
     */
    @ApiModelProperty(value = "提交时间")
    private Date createTime;

    @ApiModelProperty(value = "编号")
    private String gbNo;

    @ApiModelProperty(value = "表单id")
    private Long formId;

    @ApiModelProperty(value = "表单类型")
    private Integer formType;

    @ApiModelProperty(value = "转发记录id")
    private Long forwardHistoryId;
}
