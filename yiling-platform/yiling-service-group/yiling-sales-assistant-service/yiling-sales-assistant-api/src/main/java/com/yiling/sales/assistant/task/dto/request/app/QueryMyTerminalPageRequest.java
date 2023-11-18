package com.yiling.sales.assistant.task.dto.request.app;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/10/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMyTerminalPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -7770927934302417675L;

    private Long userTaskId;
}