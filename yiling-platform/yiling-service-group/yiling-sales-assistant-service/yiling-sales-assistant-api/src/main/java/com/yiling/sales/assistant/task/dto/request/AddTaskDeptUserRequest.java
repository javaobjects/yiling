package com.yiling.sales.assistant.task.dto.request;

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
public class AddTaskDeptUserRequest extends BaseRequest {

    private static final long serialVersionUID = 5486070479062378010L;

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