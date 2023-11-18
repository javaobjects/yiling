package com.yiling.sales.assistant.task.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务-部门参与人员关系表
 * </p>
 *
 * @author gxl
 * @date 2021-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_task_dept_user")
public class TaskDeptUserDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private Long userId;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;
}
