package com.yiling.sjms.workflow.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 审批记录
 * @author gxl
 */
@Data
public class AuditHistoryVO extends BaseVO {

    /**
     * 流程ID
     */
    @ApiModelProperty(value = "流程定义ID")
    private String procDefId;
    /**
     * 活动ID
     */
    @ApiModelProperty(value = "活动ID")
    private String activityId;
    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String activityName;

    /**
     * 活动耗时
     */
    @ApiModelProperty(value = "活动耗时")
    private String duration;

    /**
     * 执行人名称
     */
    @ApiModelProperty(value = "执行人名称")
    private String assigneeName;
    /**
     * 审批时间
     */
    @ApiModelProperty(value = "审批时间")
    private Date endTime;

    /**
     * 评论
     */
    @ApiModelProperty(value = "评论")
    private String comment;
    /**
     * 评论类型 FlowCommentEnum
     */
    @ApiModelProperty(value = "评论类型字典 comment_type")
    private String commentType;
}