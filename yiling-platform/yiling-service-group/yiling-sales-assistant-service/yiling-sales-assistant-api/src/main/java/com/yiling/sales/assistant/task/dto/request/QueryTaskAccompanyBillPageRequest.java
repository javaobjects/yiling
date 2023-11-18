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
public class QueryTaskAccompanyBillPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 4480333671594886434L;
    private Long userTaskId;
}