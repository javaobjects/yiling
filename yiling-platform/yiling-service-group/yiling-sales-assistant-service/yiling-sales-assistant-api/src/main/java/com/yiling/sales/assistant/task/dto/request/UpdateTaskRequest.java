package com.yiling.sales.assistant.task.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateTaskRequest extends BaseRequest {

    private static final long serialVersionUID = 2108740023169586638L;
    private Long taskId;

    private String taskName;

    private String taskDesc;

    private Integer neverExpires;

    private Date startTime;

    private Date endTime;

    private List<UpdateTaskRuleRequest> updateTaskRuleList;

    private List<UpdateTaskGoodsRelationRequest> updateTaskGoodsRelationList;



    private List<AddTaskAreaRequest>     updateTaskAreaList;
    private List<UpdateTaskDeptUserRequest> updateTaskDeptUserList;

    private Integer fullCover;

    private List<UpdateTaskDistributorRequest> updateTaskDistributorList;

    private UpdateTaskMemberRequest updateTaskMember;

    private List<AddTaskRuleRequest> addTaskRuleList;

    /**
     * 部门
     */
    private List<Long> deptIdList;


    /**
     * 企业类型 多个逗号分隔
     */
    private String enterpriseType;

}