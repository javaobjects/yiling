package com.yiling.dataflow.relation.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.relation.api.FlowGoodsPriceRelationApi;
import com.yiling.dataflow.relation.dto.FlowGoodsPriceRelationDTO;
import com.yiling.dataflow.relation.dto.request.SaveFlowGoodsPriceRelationRequest;
import com.yiling.dataflow.relation.service.FlowGoodsPriceRelationService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author: shuang.zhang
 * @date: 2022/6/14
 */
@DubboService
public class FlowGoodsPriceRelationApiImpl implements FlowGoodsPriceRelationApi {

    @Autowired
    private FlowGoodsPriceRelationService flowGoodsPriceRelationService;

    @Override
    public FlowGoodsPriceRelationDTO getByGoodsNameAndSpec(String goodsName, String spec) {
        return PojoUtils.map(flowGoodsPriceRelationService.getByGoodsNameAndSpec(goodsName,spec),FlowGoodsPriceRelationDTO.class);
    }

    @Override
    public FlowGoodsPriceRelationDTO getByGoodsNameAndSpecAndEnterpriseCode(String goodsName, String spec, String enterpriseCode) {
        return flowGoodsPriceRelationService.getByGoodsNameAndSpecAndEnterpriseCode(goodsName,spec,enterpriseCode);
    }

    @Override
    public boolean save(SaveFlowGoodsPriceRelationRequest request) {
        return flowGoodsPriceRelationService.save(request);
    }
}
