package com.yiling.workflow.workflow.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/12/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ProcessDetailDTO extends BaseDTO {

    private static final long serialVersionUID = 9111787135753398061L;

    /**
     * 流程ID
     */
    private String procDefId;
    /**
     * 活动ID
     */
    private String activityId;
    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动耗时
     */
    private String duration;

    /**
     * 执行人名称
     */
    private String assigneeName;
    /**
     * 审批时间
     */
    private Date endTime;

    /**
     * 评论
     */
    private String comment;
    /**
     * 评论类型 FlowCommentEnum
     */
    private String commentType;
}