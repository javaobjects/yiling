package com.yiling.sales.assistant.task.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2021/09/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryLockTerminalListRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -9063863502708743450L;

    /**
     * 用户任务id
     */
    private Long userTaskId;
}
