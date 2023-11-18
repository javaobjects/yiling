package com.yiling.sales.assistant.task.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTaskRegisterUserRequest extends QueryPageListRequest {
    private static final long serialVersionUID = 1480042721784258610L;
    private Long userTaskId;
}