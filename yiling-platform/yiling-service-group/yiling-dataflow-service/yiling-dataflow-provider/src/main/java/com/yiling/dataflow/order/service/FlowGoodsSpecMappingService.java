package com.yiling.dataflow.order.service;

import java.util.List;

import com.yiling.dataflow.order.dto.request.SaveFlowGoodsSpecMappingRequest;
import com.yiling.dataflow.order.entity.FlowGoodsSpecMappingDO;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2022/7/18
 */
public interface FlowGoodsSpecMappingService extends BaseService<FlowGoodsSpecMappingDO> {

    FlowGoodsSpecMappingDO findByGoodsNameAndSpec(String goodsName, String spec,String manufacturer);

    boolean saveFlowGoodsSpecMapping(SaveFlowGoodsSpecMappingRequest request);

    void updateRecommendInfoByGoodsNameAndSpec();

    void flushGoodsSpecificationId(List<FlushGoodsSpecIdRequest.FlushDataRequest> flushDataList);

    boolean isUsedSpecificationId(Long specificationId);
}
