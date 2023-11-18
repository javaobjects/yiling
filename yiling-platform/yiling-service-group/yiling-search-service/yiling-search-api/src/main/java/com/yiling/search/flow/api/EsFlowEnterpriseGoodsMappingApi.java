package com.yiling.search.flow.api;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.search.flow.dto.EsFlowEnterpriseGoodsMappingDTO;
import com.yiling.search.flow.dto.request.EsFlowEnterpriseGoodsMappingSearchRequest;

/**
 * @author shichen
 * @类名 EsFlowEnterpriseGoodsMappingApi
 * @描述
 * @创建时间 2023/2/28
 * @修改人 shichen
 * @修改时间 2023/2/28
 **/
public interface EsFlowEnterpriseGoodsMappingApi {

    EsAggregationDTO<EsFlowEnterpriseGoodsMappingDTO> searchFlowEnterpriseGoodsMapping(EsFlowEnterpriseGoodsMappingSearchRequest request);
}
