package com.yiling.workflow.workflow.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author gxl
 * @date 2023-05-23
 */
@Data
@Accessors(chain = true)
@TableName("ACT_HI_TASKINST")
public class ActHiTaskinstDO  {

    private static final long serialVersionUID = 1L;

    @TableId("ID_")
    private String id;

    @TableField("REV_")
    private Integer rev;

    @TableField("PROC_DEF_ID_")
    private String procDefId;

    @TableField("TASK_DEF_ID_")
    private String taskDefId;

    @TableField("TASK_DEF_KEY_")
    private String taskDefKey;

    @TableField("PROC_INST_ID_")
    private String procInstId;

    @TableField("EXECUTION_ID_")
    private String executionId;

    @TableField("SCOPE_ID_")
    private String scopeId;

    @TableField("SUB_SCOPE_ID_")
    private String subScopeId;

    @TableField("SCOPE_TYPE_")
    private String scopeType;

    @TableField("SCOPE_DEFINITION_ID_")
    private String scopeDefinitionId;

    @TableField("PROPAGATED_STAGE_INST_ID_")
    private String propagatedStageInstId;

    @TableField("NAME_")
    private String name;

    @TableField("PARENT_TASK_ID_")
    private String parentTaskId;

    @TableField("DESCRIPTION_")
    private String description;

    @TableField("OWNER_")
    private String owner;

    @TableField("ASSIGNEE_")
    private String assignee;

    @TableField("START_TIME_")
    private Date startTime;

    @TableField("CLAIM_TIME_")
    private Date claimTime;

    @TableField("END_TIME_")
    private Date endTime;

    @TableField("DURATION_")
    private Long duration;

    @TableField("DELETE_REASON_")
    private String deleteReason;

    @TableField("PRIORITY_")
    private Integer priority;

    @TableField("DUE_DATE_")
    private Date dueDate;

    @TableField("FORM_KEY_")
    private String formKey;

    @TableField("CATEGORY_")
    private String category;

    @TableField("TENANT_ID_")
    private String tenantId;

    @TableField("LAST_UPDATED_TIME_")
    private Date lastUpdatedTime;


}
