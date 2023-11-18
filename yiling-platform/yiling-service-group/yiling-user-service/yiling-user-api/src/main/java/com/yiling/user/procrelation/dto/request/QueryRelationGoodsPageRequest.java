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
public class QueryRelationGoodsPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 4077356417755788027L;

    /**
     * pop采购关系id
     */
    private Long relationId;
}
