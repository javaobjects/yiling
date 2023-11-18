package com.yiling.search.flow.api.impl;

import java.util.List;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.search.flow.api.EsFlowSaleApi;
import com.yiling.search.flow.dto.EsFlowSaleDTO;
import com.yiling.search.flow.dto.EsScrollDTO;
import com.yiling.search.flow.dto.request.EsFlowSaleScrollRequest;
import com.yiling.search.flow.dto.request.EsFlowSaleSearchRequest;
import com.yiling.search.flow.dto.request.QueryScrollRequest;
import com.yiling.search.flow.entity.EsFlowSaleEntity;
import com.yiling.search.flow.service.EsFlowSaleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
@DubboService
@Slf4j
public class EsFlowSaleApiImpl implements EsFlowSaleApi {

    @Autowired
    private EsFlowSaleService esFlowSaleService;

    @Override
    public Boolean updateFlowSale(List<EsFlowSaleDTO> request) {
        return esFlowSaleService.updateFlowSale(PojoUtils.map(request,EsFlowSaleEntity.class));
    }

    @Override
    public EsAggregationDTO<EsFlowSaleDTO> searchFlowSale(EsFlowSaleSearchRequest request) {
        return esFlowSaleService.searchFlowSale(request);
    }

    @Override
    public EsScrollDTO<EsFlowSaleDTO> scrollFlowSaleFirst(EsFlowSaleScrollRequest request) {
        return esFlowSaleService.scrollFlowSaleFirst(request);
    }

    @Override
    public EsScrollDTO<EsFlowSaleDTO> scrollFlowSaleContinue(QueryScrollRequest request) {
        return esFlowSaleService.scrollFlowSaleContinue(request);
    }
}
