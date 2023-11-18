package com.yiling.search.flow.api;

import java.util.List;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.search.flow.dto.EsFlowSaleDTO;
import com.yiling.search.flow.dto.EsScrollDTO;
import com.yiling.search.flow.dto.request.EsFlowSaleScrollRequest;
import com.yiling.search.flow.dto.request.EsFlowSaleSearchRequest;
import com.yiling.search.flow.dto.request.QueryScrollRequest;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
public interface EsFlowSaleApi {
    /**
     * 刷新销售流向数据
     *
     * @param request
     * @return
     */
    Boolean updateFlowSale(List<EsFlowSaleDTO> request);

    /**
     * Elasticsearch search flowsale
     * @param request
     * @return
     */
    EsAggregationDTO<EsFlowSaleDTO> searchFlowSale(EsFlowSaleSearchRequest request);

    EsScrollDTO<EsFlowSaleDTO> scrollFlowSaleFirst(EsFlowSaleScrollRequest request);

    EsScrollDTO<EsFlowSaleDTO> scrollFlowSaleContinue(QueryScrollRequest request);
}
