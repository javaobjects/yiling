package com.yiling.workflow.workflow.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author gxl
 * @date 2023-05-24
 */
@Data
@Accessors(chain = true)
@TableName("ACT_RU_EXECUTION")
public class ActRuExecutionDO  {


    @TableId("ID_")
    private String id;

    @TableField("REV_")
    private Integer rev;

    @TableField("PROC_INST_ID_")
    private String procInstId;

    @TableField("BUSINESS_KEY_")
    private String businessKey;

    @TableField(value="PARENT_ID_",updateStrategy = FieldStrategy.IGNORED)
    private String parentId;

    @TableField("PROC_DEF_ID_")
    private String procDefId;

    @TableField("SUPER_EXEC_")
    private String superExec;

    @TableField("ROOT_PROC_INST_ID_")
    private String rootProcInstId;

    @TableField("ACT_ID_")
    private String actId;

    @TableField("IS_ACTIVE_")
    private Integer isActive;

    @TableField("IS_CONCURRENT_")
    private Integer isConcurrent;

    @TableField("IS_SCOPE_")
    private Integer isScope;

    @TableField("IS_EVENT_SCOPE_")
    private Integer isEventScope;

    @TableField("IS_MI_ROOT_")
    private Integer isMiRoot;

    @TableField("SUSPENSION_STATE_")
    private Integer suspensionState;

    @TableField("CACHED_ENT_STATE_")
    private Integer cachedEntState;

    @TableField("TENANT_ID_")
    private String tenantId;

    @TableField("NAME_")
    private String name;

    @TableField("START_ACT_ID_")
    private String startActId;

    @TableField("START_TIME_")
    private Date startTime;

    @TableField("START_USER_ID_")
    private String startUserId;

    @TableField("LOCK_TIME_")
    private Date lockTime;

    @TableField("LOCK_OWNER_")
    private String lockOwner;

    @TableField("IS_COUNT_ENABLED_")
    private Integer isCountEnabled;

    @TableField("EVT_SUBSCR_COUNT_")
    private Integer evtSubscrCount;

    @TableField("TASK_COUNT_")
    private Integer taskCount;

    @TableField("JOB_COUNT_")
    private Integer jobCount;

    @TableField("TIMER_JOB_COUNT_")
    private Integer timerJobCount;

    @TableField("SUSP_JOB_COUNT_")
    private Integer suspJobCount;

    @TableField("DEADLETTER_JOB_COUNT_")
    private Integer deadletterJobCount;

    @TableField("EXTERNAL_WORKER_JOB_COUNT_")
    private Integer externalWorkerJobCount;

    @TableField("VAR_COUNT_")
    private Integer varCount;

    @TableField("ID_LINK_COUNT_")
    private Integer idLinkCount;

    @TableField("CALLBACK_ID_")
    private String callbackId;

    @TableField("CALLBACK_TYPE_")
    private String callbackType;

    @TableField("REFERENCE_ID_")
    private String referenceId;

    @TableField("REFERENCE_TYPE_")
    private String referenceType;

    @TableField("PROPAGATED_STAGE_INST_ID_")
    private String propagatedStageInstId;

    @TableField("BUSINESS_STATUS_")
    private String businessStatus;


}
