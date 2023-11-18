package com.yiling.user.procrelation.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryProcGoodsSnapshotPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -7400445660383637241L;

    /**
     * pop采购关系快照表id
     */
    private Long relationSnapshotId;
}
