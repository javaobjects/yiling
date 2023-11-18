package com.yiling.workflow.workflow.dto.request;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 流程任务业务对象
 *
 * @author KonBAI
 * @createTime 2022/3/10 00:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CompleteWfTaskRequest extends BaseRequest {

    private static final long serialVersionUID = -7686718808899879093L;
    /**
     * 任务Id
     */
    private String taskId;


    /**
     * 任务意见
     */
    private String comment;
    /**
     * 驳回节点id
     */
    private String nodeId;
    /**
     * 流程变量信息
     */
    private Map<String, Object> variables;
    /**
     * 审批人
     */
    private String assignee;
    /**
     * 候选人
     */
    private List<String> candidateUsers;
    /**
     * 审批组
     */
    private List<String> candidateGroups;
    /**
     * 抄送用户Id
     */
    private String copyUserIds;

    /**
     * 团购ID
     */
    private Long gbId;

/*    *//**
     * 核实团购性质：1-普通团购 2-政府采购
     *//*
    private Integer  gbReviewType;

    *//**
     * 是否地级市下机构：1-是 2-否
     *//*
    private Integer gbCityBelow;*/
}
