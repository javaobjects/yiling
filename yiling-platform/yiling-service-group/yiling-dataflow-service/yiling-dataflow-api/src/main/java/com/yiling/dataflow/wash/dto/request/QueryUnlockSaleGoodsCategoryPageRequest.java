package com.yiling.dataflow.wash.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2023/5/18
 */
@Data
public class QueryUnlockSaleGoodsCategoryPageRequest extends QueryPageListRequest {

    private Long ruleId;

    private String name;
}
