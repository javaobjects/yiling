package com.yiling.open.erp.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryErpShopMappingPageRequest
 * @描述
 * @创建时间 2023/3/21
 * @修改人 shichen
 * @修改时间 2023/3/21
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryErpShopMappingPageRequest extends QueryPageListRequest {

    /**
     * 总店企业id
     */
    private Long mainShopEid;

    /**
     * 门店企业id
     */
    private Long shopEid;
}
