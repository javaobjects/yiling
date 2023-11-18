package com.yiling.dataflow.agency.service;

import java.util.List;

import com.yiling.dataflow.agency.dto.CrmDepartmentProductRelationDTO;
import com.yiling.dataflow.agency.dto.request.SaveOrUpdateCrmDepartProductGroupRequest;
import com.yiling.dataflow.agency.entity.CrmDepartmentProductRelationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 业务部门与产品组对应关系表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-16
 */
public interface CrmDepartmentProductRelationService extends BaseService<CrmDepartmentProductRelationDO> {

    List<CrmDepartmentProductRelationDO> getByIds(List<Long>ids,String tableSuffix);

    List<CrmDepartmentProductRelationDO> getByIdList(List<Long> ids);

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

    /**
     * 通过商品组ids查询商品组部门关联关系集合
     * @param groupIds
     * @return
     */
    List<CrmDepartmentProductRelationDTO> getBakByGroupIds(List<Long> groupIds,String tableSuffix);

    /**
     * 批量保存部门商品组对应关系
     * @param requestList
     * @param groupId
     * @param opUserId
     * @return
     */
    Boolean batchSaveRelationByGroup(List<SaveOrUpdateCrmDepartProductGroupRequest> requestList,Long groupId,String groupName,Long opUserId);
}
