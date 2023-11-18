package com.yiling.search.flow.api.impl;

import java.util.List;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.search.flow.api.EsFlowPurchaseApi;
import com.yiling.search.flow.api.EsFlowSaleApi;
import com.yiling.search.flow.dto.EsFlowPurchaseDTO;
import com.yiling.search.flow.dto.EsFlowSaleDTO;
import com.yiling.search.flow.dto.EsScrollDTO;
import com.yiling.search.flow.dto.request.EsFlowPurchaseScrollRequest;
import com.yiling.search.flow.dto.request.EsFlowPurchaseSearchRequest;
import com.yiling.search.flow.dto.request.EsFlowSaleSearchRequest;
import com.yiling.search.flow.dto.request.QueryScrollRequest;
import com.yiling.search.flow.entity.EsFlowPurchaseEntity;
import com.yiling.search.flow.entity.EsFlowSaleEntity;
import com.yiling.search.flow.service.EsFlowPurchaseService;
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
public class EsFlowPurchaseApiImpl implements EsFlowPurchaseApi {

    @Autowired
    private EsFlowPurchaseService esFlowPurchaseService;

    @Override
    public Boolean updateFlowPurchase(List<EsFlowPurchaseDTO> request) {
        return esFlowPurchaseService.updateFlowPurchase(PojoUtils.map(request, EsFlowPurchaseEntity.class));
    }

    @Override
    public EsAggregationDTO<EsFlowPurchaseDTO> searchFlowPurchase(EsFlowPurchaseSearchRequest request) {
        return esFlowPurchaseService.searchFlowPurchase(request);
    }

    @Override
    public EsScrollDTO<EsFlowPurchaseDTO> scrollFlowPurchaseFirst(EsFlowPurchaseScrollRequest request) {
        return esFlowPurchaseService.scrollFlowPurchaseFirst(request);
    }

    @Override
    public EsScrollDTO<EsFlowPurchaseDTO> scrollFlowPurchaseContinue(QueryScrollRequest request) {
        return esFlowPurchaseService.scrollFlowPurchaseContinue(request);
    }
}
