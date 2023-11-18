package com.yiling.search.flow.service;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.search.flow.dto.EsFlowEnterpriseGoodsMappingDTO;
import com.yiling.search.flow.dto.request.EsFlowEnterpriseGoodsMappingSearchRequest;


/**
 * @author shichen
 * @类名 EsFlowEnterpriseGoodsMappingService
 * @描述
 * @创建时间 2023/2/28
 * @修改人 shichen
 * @修改时间 2023/2/28
 **/
public interface EsFlowEnterpriseGoodsMappingService {

    EsAggregationDTO<EsFlowEnterpriseGoodsMappingDTO> searchFlowEnterpriseGoodsMapping(EsFlowEnterpriseGoodsMappingSearchRequest request);
}
