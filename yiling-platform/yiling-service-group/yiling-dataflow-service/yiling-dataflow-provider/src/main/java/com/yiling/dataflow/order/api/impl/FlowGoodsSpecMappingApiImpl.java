package com.yiling.dataflow.order.api.impl;

import com.yiling.dataflow.order.dto.request.SaveFlowGoodsSpecMappingRequest;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.order.api.FlowGoodsSpecMappingApi;
import com.yiling.dataflow.order.dto.FlowGoodsSpecMappingDTO;
import com.yiling.dataflow.order.service.FlowGoodsSpecMappingService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author fucheng.bai
 * @date 2022/7/18
 */
@DubboService
public class FlowGoodsSpecMappingApiImpl implements FlowGoodsSpecMappingApi {

    @Autowired
    private FlowGoodsSpecMappingService flowGoodsSpecMappingService;

    @Override
    public FlowGoodsSpecMappingDTO findByGoodsNameAndSpec(String goodsName, String spec,String manufacturer) {
        return PojoUtils.map(flowGoodsSpecMappingService.findByGoodsNameAndSpec(goodsName, spec,manufacturer), FlowGoodsSpecMappingDTO.class);
    }

    @Override
    public boolean saveFlowGoodsSpecMapping(SaveFlowGoodsSpecMappingRequest request) {
        return flowGoodsSpecMappingService.saveFlowGoodsSpecMapping(request);
    }

    @Override
    public void updateRecommendInfoByGoodsNameAndSpec() {
        flowGoodsSpecMappingService.updateRecommendInfoByGoodsNameAndSpec();
    }
}
