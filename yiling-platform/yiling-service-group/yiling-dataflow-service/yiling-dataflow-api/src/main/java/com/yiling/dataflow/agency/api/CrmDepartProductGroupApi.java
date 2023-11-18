package com.yiling.dataflow.agency.api;

import com.yiling.dataflow.agency.dto.CrmDepartmentProductRelationDTO;
import com.yiling.dataflow.agency.dto.request.QueryDepartProductGroupByUploadNameRequest;
import com.yiling.dataflow.crm.dto.CrmDepartmentAreaRelationDTO;

import java.util.List;

/**
 * 业务部门和产品组对应关系
 */
public interface CrmDepartProductGroupApi {
    List<CrmDepartmentProductRelationDTO> getDepartProductGroupListByUploadName(QueryDepartProductGroupByUploadNameRequest request);

    /**
     * 通过商品组id查询商品组部门关联关系集合
     * @param groupId
     * @return
     */
    List<CrmDepartmentProductRelationDTO> getByGroupId(Long groupId);

    /**
     * 通过商品组ids查询商品组部门关联关系集合
     * @param groupIds
     * @return
     */
    List<CrmDepartmentProductRelationDTO> getByGroupIds(List<Long> groupIds);

}
