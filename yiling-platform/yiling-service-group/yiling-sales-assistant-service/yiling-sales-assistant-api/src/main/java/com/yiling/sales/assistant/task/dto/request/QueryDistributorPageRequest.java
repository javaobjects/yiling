package com.yiling.sales.assistant.task.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: ray
 * @date: 2021/10/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryDistributorPageRequest extends QueryPageListRequest {
    private static final long serialVersionUID = -8909965024708052632L;
    private Integer type;
}