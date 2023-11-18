package com.yiling.workflow.workflow.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("ACT_RU_TASK")
public class ActRuTaskDO  {

    private static final long serialVersionUID = 1L;

    @TableId("ID_")
    private String id;

    @TableField("REV_")
    private Integer rev;

    @TableField("EXECUTION_ID_")
    private String executionId;

    @TableField("PROC_INST_ID_")
    private String procInstId;

    @TableField("PROC_DEF_ID_")
    private String procDefId;

    @TableField("TASK_DEF_ID_")
    private String taskDefId;

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

    @TableField("TASK_DEF_KEY_")
    private String taskDefKey;

    @TableField("OWNER_")
    private String owner;

    @TableField("ASSIGNEE_")
    private String assignee;

    @TableField("DELEGATION_")
    private String delegation;

    @TableField("PRIORITY_")
    private Integer priority;

    @TableField("CREATE_TIME_")
    private Date createTime;

    @TableField("DUE_DATE_")
    private Date dueDate;

    @TableField("CATEGORY_")
    private String category;

    @TableField("SUSPENSION_STATE_")
    private Integer suspensionState;

    @TableField("TENANT_ID_")
    private String tenantId;

    @TableField("FORM_KEY_")
    private String formKey;

    @TableField("CLAIM_TIME_")
    private Date claimTime;

    @TableField("IS_COUNT_ENABLED_")
    private Integer isCountEnabled;

    @TableField("VAR_COUNT_")
    private Integer varCount;

    @TableField("ID_LINK_COUNT_")
    private Integer idLinkCount;

    @TableField("SUB_TASK_COUNT_")
    private Integer subTaskCount;


}
