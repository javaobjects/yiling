package com.yiling.dataflow.flow.api;

import java.util.List;

import com.yiling.dataflow.flow.dto.FlowEnterpriseGoodsMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseGoodsMappingRequest;

/**
 * @author shichen
 * @类名 FlowEnterpriseGoodsMappingApi
 * @描述
 * @创建时间 2023/2/28
 * @修改人 shichen
 * @修改时间 2023/2/28
 **/
public interface FlowEnterpriseGoodsMappingApi {

    Long save(SaveFlowEnterpriseGoodsMappingRequest request);

    Boolean deleteById(Long id,Long opUserId);

    FlowEnterpriseGoodsMappingDTO findById(Long id);

    List<FlowEnterpriseGoodsMappingDTO> findByIds(List<Long> ids);

    /**
     * 原始商品名称，原始商品规格和经销商crmId查询 唯一商品对照
     * @param flowGoodsName
     * @param flowSpecification
     * @param crmEnterpriseId
     * @return
     */
    FlowEnterpriseGoodsMappingDTO findByFlowGoodsNameAndFlowSpecificationAndCrmEnterpriseId(String flowGoodsName,String flowSpecification,Long crmEnterpriseId);

    /**
     * 批量保存修改
     * @param requestList
     * @return
     */
    Boolean batchUpdateById(List<SaveFlowEnterpriseGoodsMappingRequest> requestList);

    /**
     * idList发送刷新商品流向mq
     * @param mappingList
     * @return
     */
    Boolean sendRefreshGoodsFlowMq(List<FlowEnterpriseGoodsMappingDTO> mappingList);

    /**
     * 根据经销商crmId、标准商品编码查询 唯一商品对照
     *
     * @param crmEnterpriseId
     * @param crmGoodsCode
     * @return
     */
    FlowEnterpriseGoodsMappingDTO findByCrmEnterpriseIdAndCrmGoodsCode(Long crmEnterpriseId, Long crmGoodsCode);

}
