package com.yiling.sales.assistant.task.dto.request;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/14
 */
@Data
@Accessors(chain = true)
public class UpdateTaskDeptUserRequest implements Serializable {
    private static final long serialVersionUID = 8121657407887196593L;
    private Long userId;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 任务id
     */
    private Long taskId;
}