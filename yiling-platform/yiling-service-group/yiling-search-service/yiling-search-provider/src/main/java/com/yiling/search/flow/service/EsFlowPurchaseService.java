package com.yiling.search.flow.service;

import java.util.List;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.search.flow.dto.EsFlowPurchaseDTO;
import com.yiling.search.flow.dto.EsFlowSaleDTO;
import com.yiling.search.flow.dto.EsScrollDTO;
import com.yiling.search.flow.dto.request.EsFlowPurchaseScrollRequest;
import com.yiling.search.flow.dto.request.EsFlowPurchaseSearchRequest;
import com.yiling.search.flow.dto.request.EsFlowSaleScrollRequest;
import com.yiling.search.flow.dto.request.QueryScrollRequest;
import com.yiling.search.flow.entity.EsFlowPurchaseEntity;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
public interface EsFlowPurchaseService {

    /**
     * 刷新库存
     *
     * @param request
     * @return
     */
    Boolean updateFlowPurchase(List<EsFlowPurchaseEntity> request);

    EsAggregationDTO<EsFlowPurchaseDTO> searchFlowPurchase(EsFlowPurchaseSearchRequest request);

    EsScrollDTO<EsFlowPurchaseDTO> scrollFlowPurchaseFirst(EsFlowPurchaseScrollRequest request);

    EsScrollDTO<EsFlowPurchaseDTO> scrollFlowPurchaseContinue(QueryScrollRequest request);
}
