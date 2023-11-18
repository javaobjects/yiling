package com.yiling.search.flow.api;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.search.flow.dto.EsFlowEnterpriseCustomerMappingDTO;
import com.yiling.search.flow.dto.request.EsFlowEnterpriseCustomerMappingSearchRequest;

/**
 * @author shichen
 * @类名 EsFlowEnterpriseCustomerMappingApi
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
public interface EsFlowEnterpriseCustomerMappingApi {

    /**
     * 搜索客户对照关系
     * @param request
     * @return
     */
    EsAggregationDTO<EsFlowEnterpriseCustomerMappingDTO> searchFlowEnterpriseCustomerMapping(EsFlowEnterpriseCustomerMappingSearchRequest request);
}
