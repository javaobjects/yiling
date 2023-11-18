package com.yiling.dataflow.flow.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flow.dto.FlowEnterpriseCustomerMappingDTO;
import com.yiling.dataflow.flow.dto.FlowEnterpriseSupplierMappingDTO;
import com.yiling.dataflow.flow.dto.request.QueryFlowEnterpriseSupplierMappingPageRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseCustomerMappingRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseSupplierMappingRequest;

/**
 * @author shichen
 * @类名 FlowEnterpriseSupplierMappingApi
 * @描述
 * @创建时间 2023/5/31
 * @修改人 shichen
 * @修改时间 2023/5/31
 **/
public interface FlowEnterpriseSupplierMappingApi {

    Long save(SaveFlowEnterpriseSupplierMappingRequest request);

    Boolean deleteById(Long id,Long opUserId);

    FlowEnterpriseSupplierMappingDTO findById(Long id);

    List<FlowEnterpriseSupplierMappingDTO> findByIds(List<Long> ids);

    /**
     * 分页查询供应商对照关系
     * @param request
     * @return
     */
    Page<FlowEnterpriseSupplierMappingDTO> pageList(QueryFlowEnterpriseSupplierMappingPageRequest request);

    /**
     * 保存配送商对照关系
     * @param request
     * @return
     */
    Long saveOrUpdate(SaveFlowEnterpriseSupplierMappingRequest request);

    /**
     * 通过原始配送商名称和经销商id查询唯一对照关系
     * @param flowSupplierName
     * @param crmEnterpriseId
     * @return
     */
    FlowEnterpriseSupplierMappingDTO findBySupplierNameAndCrmEnterpriseId(String flowSupplierName, Long crmEnterpriseId);

    /**
     * 批量保存修改
     * @param requestList
     * @return
     */
    Boolean batchUpdateById(List<SaveFlowEnterpriseSupplierMappingRequest> requestList);

    /**
     * idList发送刷新供应商流向mq
     * @param mappingList
     * @return
     */
    Boolean sendRefreshSupplierFlowMq(List<FlowEnterpriseSupplierMappingDTO> mappingList);
}
