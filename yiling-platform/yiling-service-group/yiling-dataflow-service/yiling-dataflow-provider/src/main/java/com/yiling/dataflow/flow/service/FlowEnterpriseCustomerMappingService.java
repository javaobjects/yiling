package com.yiling.dataflow.flow.service;

import java.util.List;

import com.yiling.dataflow.flow.dto.FlowEnterpriseCustomerMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseCustomerMappingRequest;
import com.yiling.dataflow.flow.entity.FlowEnterpriseCustomerMappingDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author shichen
 * @类名 FlowEnterpriseCustomerMappingService
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
public interface FlowEnterpriseCustomerMappingService extends BaseService<FlowEnterpriseCustomerMappingDO> {

    Long saveOrUpdate(SaveFlowEnterpriseCustomerMappingRequest request);

    FlowEnterpriseCustomerMappingDTO findByCustomerNameAndCrmEnterpriseId(String flowCustomerName,Long crmEnterpriseId);

    /**
     * 批量保存修改
     * @param requestList
     * @return
     */
    Boolean batchUpdateById(List<SaveFlowEnterpriseCustomerMappingRequest> requestList);

    /**
     * idList发送刷新客户流向mq
     * @param mappingList
     * @return
     */
    Boolean sendRefreshCustomerFlowMq(List<FlowEnterpriseCustomerMappingDTO> mappingList);

    /**
     * 原始流向名称查询未映射数据
     * @param flowCustomerName
     * @return
     */
    List<FlowEnterpriseCustomerMappingDTO> findUnmappedByFlowCustomerName(String flowCustomerName);
}
