package com.yiling.export.export.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryExportPageListRequest extends QueryPageListRequest {
    private static final long serialVersionUID = 1515883924920299249L;
    /**
     * 操作用户
     */
    private Long userId;
}
