package com.yiling.dataflow.flow.service;

import java.util.List;

import com.yiling.dataflow.flow.dto.FlowEnterpriseGoodsMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseGoodsMappingRequest;
import com.yiling.dataflow.flow.entity.FlowEnterpriseGoodsMappingDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author shichen
 * @类名 FlowEnterpriseGoodsMappingService
 * @描述
 * @创建时间 2023/2/27
 * @修改人 shichen
 * @修改时间 2023/2/27
 **/
public interface FlowEnterpriseGoodsMappingService  extends BaseService<FlowEnterpriseGoodsMappingDO> {

    Long saveOrUpdate(SaveFlowEnterpriseGoodsMappingRequest request);

    FlowEnterpriseGoodsMappingDTO findByFlowGoodsNameAndFlowSpecificationAndCrmEnterpriseId(String flowGoodsName,String flowSpecification,Long crmEnterpriseId);

    /**
     * 批量保存修改
     * @param requestList
     * @return
     */
    Boolean batchUpdateById(List<SaveFlowEnterpriseGoodsMappingRequest> requestList);

    /**
     * idList发送刷新产品流向mq
     * @param mappingList
     * @return
     */
    Boolean sendRefreshGoodsFlowMq(List<FlowEnterpriseGoodsMappingDTO> mappingList);

    /**
     * 跟经销商crmId、标准商品编码查询 唯一商品对照
     *
     * @param crmEnterpriseId
     * @param crmGoodsCode
     * @return
     */
    FlowEnterpriseGoodsMappingDTO findByCrmEnterpriseIdAndCrmGoodsCode(Long crmEnterpriseId, Long crmGoodsCode);
}
