package com.yiling.search.flow.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.search.flow.api.EsFlowEnterpriseGoodsMappingApi;
import com.yiling.search.flow.dto.EsFlowEnterpriseGoodsMappingDTO;
import com.yiling.search.flow.dto.request.EsFlowEnterpriseGoodsMappingSearchRequest;
import com.yiling.search.flow.service.EsFlowEnterpriseGoodsMappingService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 EsFlowEnterpriseGoodsMappingApiImpl
 * @描述
 * @创建时间 2023/2/28
 * @修改人 shichen
 * @修改时间 2023/2/28
 **/
@DubboService
@Slf4j
public class EsFlowEnterpriseGoodsMappingApiImpl implements EsFlowEnterpriseGoodsMappingApi {
    @Autowired
    private EsFlowEnterpriseGoodsMappingService esFlowEnterpriseGoodsMappingService;

    @Override
    public EsAggregationDTO<EsFlowEnterpriseGoodsMappingDTO> searchFlowEnterpriseGoodsMapping(EsFlowEnterpriseGoodsMappingSearchRequest request) {
        return esFlowEnterpriseGoodsMappingService.searchFlowEnterpriseGoodsMapping(request);
    }
}
