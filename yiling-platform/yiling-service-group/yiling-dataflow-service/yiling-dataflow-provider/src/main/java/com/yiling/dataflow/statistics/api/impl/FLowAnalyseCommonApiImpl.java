package com.yiling.dataflow.statistics.api.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.statistics.api.FLowAnalyseCommonApi;
import com.yiling.dataflow.statistics.dto.FlowAnalyseEnterpriseDTO;
import com.yiling.dataflow.statistics.dto.FlowAnalyseGoodsDTO;
import com.yiling.dataflow.statistics.dto.request.FlowAnalyseEnterpriseRequest;
import com.yiling.dataflow.statistics.dto.request.FlowAnalyseGoodsRequest;
import com.yiling.dataflow.statistics.service.FlowDistributionEnterpriseService;
import com.yiling.dataflow.statistics.service.FlowAnalyseGoodsService;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
@Slf4j
public class FLowAnalyseCommonApiImpl implements FLowAnalyseCommonApi {
    @Autowired
    private FlowAnalyseGoodsService           flowAnalyseGoodsService;
    @Autowired
    private FlowDistributionEnterpriseService flowDistributionEnterpriseService;
    @Override
    public Page<FlowAnalyseGoodsDTO> getGoodsListByName(FlowAnalyseGoodsRequest request) {
        return PojoUtils.map(flowAnalyseGoodsService.getGoodsListByName(request),FlowAnalyseGoodsDTO.class);
    }

    @Override
    public Page<FlowAnalyseEnterpriseDTO> getEnterpriseListByName(FlowAnalyseEnterpriseRequest request) {
        return PojoUtils.map(flowDistributionEnterpriseService.getEnterpriseListByName(request),FlowAnalyseEnterpriseDTO.class);
    }
}
