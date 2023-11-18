package com.yiling.search.flow.api;

import java.util.List;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.search.flow.dto.EsFlowGoodsBatchDetailDTO;
import com.yiling.search.flow.dto.EsScrollDTO;
import com.yiling.search.flow.dto.request.EsFlowGoodsBatchDetailScrollRequest;
import com.yiling.search.flow.dto.request.EsFlowGoodsBatchDetailSearchRequest;
import com.yiling.search.flow.dto.request.QueryScrollRequest;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
public interface EsFlowGoodsBatchDetailApi {
    /**
     * 刷新销售流向数据
     *
     * @param request
     * @return
     */
    Boolean updateFlowGoodsBatchDetail(List<EsFlowGoodsBatchDetailDTO> request);

    /**
     * Elasticsearch search flowGoodsBatchDetail
     * @param request
     * @return
     */
    EsAggregationDTO<EsFlowGoodsBatchDetailDTO> searchFlowGoodsBatchDetail(EsFlowGoodsBatchDetailSearchRequest request);

    EsScrollDTO<EsFlowGoodsBatchDetailDTO> scrollFlowGoodsBatchDetailFirst(EsFlowGoodsBatchDetailScrollRequest request);

    EsScrollDTO<EsFlowGoodsBatchDetailDTO> scrollFlowGoodsBatchDetailContinue(QueryScrollRequest request);
}
