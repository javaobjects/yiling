package com.yiling.dataflow.relation.api;

import com.yiling.dataflow.relation.dto.FlowGoodsPriceRelationDTO;
import com.yiling.dataflow.relation.dto.request.SaveFlowGoodsPriceRelationRequest;

/**
 * @author: shuang.zhang
 * @date: 2022/6/14
 */
public interface FlowGoodsPriceRelationApi {

    FlowGoodsPriceRelationDTO getByGoodsNameAndSpec(String goodsName, String spec);

    FlowGoodsPriceRelationDTO getByGoodsNameAndSpecAndEnterpriseCode(String goodsName, String spec, String enterpriseCode);

    boolean save(SaveFlowGoodsPriceRelationRequest request);
}
