package com.yiling.sales.assistant.task.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 任务详情
 * @author: ray
 * @date: 2021/9/15
 */
@Data
public class TaskDetailDTO implements Serializable {
    private static final long serialVersionUID = 1214295793213889259L;
    private Long id;

    private String taskName;

    private String taskDesc;

    private Integer taskType;

    private Integer saleType;

    private String ename;

    private String taskArea;

    private String taskDeptUser;

    private Boolean neverExpires;

    private Date startTime;

    private Date endTime;


    private String            createdBy;

    private Long createUser;

    private Date              createdTime;
    private Integer           taskStatus;
    private List<TaskRuleDTO> takeRuleVOList;

    private List<TaskRuleDTO> finishRuleVOList;

    private List<TaskRuleDTO> commissionRuleVOList;

    private List<TaskGoodsDTO> taskGoodsList;

    private  Integer fullCover;

    private Long eid;

    private String profit;

    private TaskMemberDTO taskMember;

    private TaskMemberPromotiondDTO taskMemberPromotion;

    private Integer finishType;

    private Date updatedTime;

    /**
     * 部门
     */
    private List<Long> deptIdList;

    /**
     * 企业类型 多个逗号分隔
     */
    private String enterpriseType;
}