package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/11/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCategoryPageRequest extends QueryPageListRequest {

    /**
     * 引用业务线id 1-健康管理中心，2-IH医生端，3-IH患者端
     */
    private Long lineId;

    /**
     * 板块id
     */
    private Long moduleId;
}
