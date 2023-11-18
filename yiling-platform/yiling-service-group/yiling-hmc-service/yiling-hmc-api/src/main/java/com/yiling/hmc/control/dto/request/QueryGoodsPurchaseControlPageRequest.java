package com.yiling.hmc.control.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/4/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsPurchaseControlPageRequest extends QueryPageListRequest {
    private String enterpriseName;

    /**
     * 管控表id
     */
    private Long goodsControlId;

    private List<Long> eidList;
}