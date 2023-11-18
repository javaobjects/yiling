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
public class QuerySpecByBuyerPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -1568284348129482091L;

    /**
     * 渠道商eid
     */
    private Long buyerEid;
}
