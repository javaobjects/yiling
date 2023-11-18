package com.yiling.search.flow.service;

import java.util.List;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.search.flow.dto.EsFlowSaleDTO;
import com.yiling.search.flow.dto.EsScrollDTO;
import com.yiling.search.flow.dto.request.EsFlowSaleScrollRequest;
import com.yiling.search.flow.dto.request.EsFlowSaleSearchRequest;
import com.yiling.search.flow.dto.request.QueryScrollRequest;
import com.yiling.search.flow.entity.EsFlowSaleEntity;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
public interface EsFlowSaleService {

    /**
     * 刷新库存
     *
     * @param request
     * @return
     */
    Boolean updateFlowSale(List<EsFlowSaleEntity> request);

    EsAggregationDTO<EsFlowSaleDTO> searchFlowSale(EsFlowSaleSearchRequest request);

    EsScrollDTO<EsFlowSaleDTO> scrollFlowSaleFirst(EsFlowSaleScrollRequest request);

    EsScrollDTO<EsFlowSaleDTO> scrollFlowSaleContinue(QueryScrollRequest request);
}
