package com.yiling.search.flow.api.impl;

import java.util.List;

import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.search.flow.api.EsFlowGoodsBatchDetailApi;
import com.yiling.search.flow.dto.EsFlowGoodsBatchDetailDTO;
import com.yiling.search.flow.dto.EsScrollDTO;
import com.yiling.search.flow.dto.request.EsFlowGoodsBatchDetailScrollRequest;
import com.yiling.search.flow.dto.request.EsFlowGoodsBatchDetailSearchRequest;
import com.yiling.search.flow.dto.request.QueryScrollRequest;
import com.yiling.search.flow.entity.EsFlowGoodsBatchDetailEntity;
import com.yiling.search.flow.service.EsFlowGoodsBatchDetailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2023/2/13
 */
@DubboService
@Slf4j
public class EsFlowGoodsBatchDetailApiImpl implements EsFlowGoodsBatchDetailApi {

    @Autowired
    private EsFlowGoodsBatchDetailService esFlowGoodsBatchDetailService;

    @Override
    public Boolean updateFlowGoodsBatchDetail(List<EsFlowGoodsBatchDetailDTO> request) {
        return esFlowGoodsBatchDetailService.updateFlowGoodsBatchDetail(PojoUtils.map(request, EsFlowGoodsBatchDetailEntity.class));
    }

    @Override
    public EsAggregationDTO<EsFlowGoodsBatchDetailDTO> searchFlowGoodsBatchDetail(EsFlowGoodsBatchDetailSearchRequest request) {
        return esFlowGoodsBatchDetailService.searchFlowGoodsBatchDetail(request);
    }

    @Override
    public EsScrollDTO<EsFlowGoodsBatchDetailDTO> scrollFlowGoodsBatchDetailFirst(EsFlowGoodsBatchDetailScrollRequest request) {
        return esFlowGoodsBatchDetailService.scrollFlowGoodsBatchDetailFirst(request);
    }

    @Override
    public EsScrollDTO<EsFlowGoodsBatchDetailDTO> scrollFlowGoodsBatchDetailContinue(QueryScrollRequest request) {
        return esFlowGoodsBatchDetailService.scrollFlowGoodsBatchDetailContinue(request);
    }
}
