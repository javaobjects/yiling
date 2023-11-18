package com.yiling.dataflow.order.api;

import com.yiling.dataflow.order.dto.FlowGoodsSpecMappingDTO;
import com.yiling.dataflow.order.dto.request.SaveFlowGoodsSpecMappingRequest;
import com.yiling.dataflow.relation.dto.request.SaveFlowSupplierMappingRequest;

/**
 * @author fucheng.bai
 * @date 2022/7/18
 */
public interface FlowGoodsSpecMappingApi {

    /**
     * 通过名称和规格查询映射关系
     * @param goodsName
     * @param spec
     * @return
     */
    FlowGoodsSpecMappingDTO findByGoodsNameAndSpec(String goodsName, String spec,String manufacturer);

    /**
     * 保存流向数据映射关系
     * @param request
     * @return
     */
    boolean saveFlowGoodsSpecMapping(SaveFlowGoodsSpecMappingRequest request);

    /**
     * 定时任务匹配商品名称和规格
     */
    void updateRecommendInfoByGoodsNameAndSpec();
}
