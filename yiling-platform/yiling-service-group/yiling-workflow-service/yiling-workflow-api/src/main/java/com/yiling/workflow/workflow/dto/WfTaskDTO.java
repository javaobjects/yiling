package com.yiling.workflow.workflow.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 工作流任务视图对象
 *
 * @author KonBAI
 * @createTime 2022/3/10 00:12
 */
@Data
@Accessors(chain = true)
public class WfTaskDTO implements Serializable {
    private static final long serialVersionUID = 5591162099326307105L;
    /**
     * 任务编号
     */
    private String taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务Key
     */
    private String taskDefKey;
    /**
     * 任务执行人Id
     */
    private String assigneeId;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 流程发起人部门名称
     */
    private String startDeptName;
    /**
     * 任务执行人名称
     */
    private String assigneeName;
    /**
     * 流程发起人Id
     */
    private String startUserId;
    /**
     * 流程发起人名称
     */
    private String startUserName;
    /**
     * 流程类型
     */
    private String category;
    /**
     * 流程变量信息
     */
    private Object procVars;
    /**
     * 局部变量信息
     */
    private Object taskLocalVars;
    /**
     * 流程部署编号
     */
    private String deployId;
    /**
     * 流程ID
     */
    private String procDefId;
    /**
     * 流程key
     */
    private String procDefKey;
    /**
     * 流程定义名称
     */
    private String procDefName;
    /**
     * 流程定义内置使用版本
     */
    private int procDefVersion;
    /**
     * 流程实例ID
     */
    private String procInsId;
    /**
     * 历史流程实例ID
     */
    private String hisProcInsId;
    /**
     * 任务耗时
     */
    private String duration;


    /**
     * 任务创建时间
     */
    private Date createTime;
    /**
     * 任务完成时间
     */
    private Date finishTime;

    /**
     * 业务系统唯一标识（比如团购编号）
     */
    private String businessKey;

    /**
     * 团购表单id
     */
    private Long gbId;

    /**
     * 团购编号
     */
    private String gbNo;

    /**
     * 是否需要市场运营部核实
     */
    private Boolean needCheck;

    /**
     * 是否已办
     */
    private Boolean isFinished;
    /**
     * form表id
     */
    private Long formId;

    private Integer formType;

    private  String executionId;
    /**
     * 转发记录id
     */
    private Long forwardHistoryId;
}
