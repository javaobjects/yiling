package com.yiling.search.flow.api;

import java.util.List;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.search.flow.dto.EsFlowPurchaseDTO;
import com.yiling.search.flow.dto.EsScrollDTO;
import com.yiling.search.flow.dto.request.EsFlowPurchaseScrollRequest;
import com.yiling.search.flow.dto.request.EsFlowPurchaseSearchRequest;
import com.yiling.search.flow.dto.request.QueryScrollRequest;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
public interface EsFlowPurchaseApi {
    /**
     * 刷新销售流向数据
     *
     * @param request
     * @return
     */
    Boolean updateFlowPurchase(List<EsFlowPurchaseDTO> request);

    /**
     * Elasticsearch search flowPurchase
     * @param request
     * @return
     */
    EsAggregationDTO<EsFlowPurchaseDTO> searchFlowPurchase(EsFlowPurchaseSearchRequest request);

    EsScrollDTO<EsFlowPurchaseDTO> scrollFlowPurchaseFirst(EsFlowPurchaseScrollRequest request);

    EsScrollDTO<EsFlowPurchaseDTO> scrollFlowPurchaseContinue(QueryScrollRequest request);
}
