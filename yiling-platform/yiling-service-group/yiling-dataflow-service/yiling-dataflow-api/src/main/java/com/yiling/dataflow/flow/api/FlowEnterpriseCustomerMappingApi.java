package com.yiling.dataflow.flow.api;

import java.util.List;

import com.yiling.dataflow.flow.dto.FlowEnterpriseCustomerMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseCustomerMappingRequest;

/**
 * @author shichen
 * @类名 FlowEnterpriseCustomerMappingApi
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
public interface FlowEnterpriseCustomerMappingApi {

    Long save(SaveFlowEnterpriseCustomerMappingRequest request);

    Boolean deleteById(Long id,Long opUserId);

    FlowEnterpriseCustomerMappingDTO findById(Long id);

    List<FlowEnterpriseCustomerMappingDTO> findByIds(List<Long> ids);

    /**
     * 原始客户名称和经销商id查询唯一客户对照
     * @param flowCustomerName
     * @param crmEnterpriseId
     * @return
     */
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
