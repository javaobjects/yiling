package com.yiling.search.flow.service;

import java.util.List;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.search.flow.dto.EsFlowGoodsBatchDetailDTO;
import com.yiling.search.flow.dto.EsScrollDTO;
import com.yiling.search.flow.dto.request.EsFlowGoodsBatchDetailScrollRequest;
import com.yiling.search.flow.dto.request.EsFlowGoodsBatchDetailSearchRequest;
import com.yiling.search.flow.dto.request.QueryScrollRequest;
import com.yiling.search.flow.entity.EsFlowGoodsBatchDetailEntity;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
public interface EsFlowGoodsBatchDetailService {

    /**
     * 刷新库存
     *
     * @param request
     * @return
     */
    Boolean updateFlowGoodsBatchDetail(List<EsFlowGoodsBatchDetailEntity> request);

    EsAggregationDTO<EsFlowGoodsBatchDetailDTO> searchFlowGoodsBatchDetail(EsFlowGoodsBatchDetailSearchRequest request);

    EsScrollDTO<EsFlowGoodsBatchDetailDTO> scrollFlowGoodsBatchDetailFirst(EsFlowGoodsBatchDetailScrollRequest request);

    EsScrollDTO<EsFlowGoodsBatchDetailDTO> scrollFlowGoodsBatchDetailContinue(QueryScrollRequest request);
}
