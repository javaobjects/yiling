package com.yiling.search.flow.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.search.flow.api.EsFlowEnterpriseCustomerMappingApi;
import com.yiling.search.flow.dto.EsFlowEnterpriseCustomerMappingDTO;
import com.yiling.search.flow.dto.request.EsFlowEnterpriseCustomerMappingSearchRequest;
import com.yiling.search.flow.service.EsFlowEnterpriseCustomerMappingService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 EsFlowEnterpriseCustomerMappingApiImpl
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@DubboService
@Slf4j
public class EsFlowEnterpriseCustomerMappingApiImpl implements EsFlowEnterpriseCustomerMappingApi {
    @Autowired
    private EsFlowEnterpriseCustomerMappingService esFlowEnterpriseCustomerMappingService;

    @Override
    public EsAggregationDTO<EsFlowEnterpriseCustomerMappingDTO> searchFlowEnterpriseCustomerMapping(EsFlowEnterpriseCustomerMappingSearchRequest request) {
        return esFlowEnterpriseCustomerMappingService.searchFlowEnterpriseCustomerMapping(request);
    }
}
