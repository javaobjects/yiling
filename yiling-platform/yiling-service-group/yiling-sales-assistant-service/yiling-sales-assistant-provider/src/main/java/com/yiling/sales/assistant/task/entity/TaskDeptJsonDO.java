package com.yiling.sales.assistant.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务部门（前端反显用）
 * </p>
 *
 * @author gxl
 * @date 2023-01-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_task_dept_json")
public class TaskDeptJsonDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private Long taskId;

    private String deptJson;


}
