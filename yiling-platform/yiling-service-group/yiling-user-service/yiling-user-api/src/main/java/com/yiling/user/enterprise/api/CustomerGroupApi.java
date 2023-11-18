package com.yiling.user.enterprise.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerGroupRequest;
import com.yiling.user.enterprise.dto.request.AddResultGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.MoveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerGroupPageListRequest;
import com.yiling.user.enterprise.dto.request.RemoveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.SaveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerGroupRequest;

/**
 * 企业客户分组 API
 *
 * @author: xuan.zhou
 * @date: 2021/5/24
 */
public interface CustomerGroupApi {

    /**
     * 客户分组分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseCustomerGroupDTO> pageList(QueryCustomerGroupPageListRequest request);

    /**
     * 添加客户分组
     *
     * @param request
     * @return
     */
    Long add(AddCustomerGroupRequest request);

    /**
     * 修改客户分组
     *
     * @param request
     * @return
     */
    boolean update(UpdateCustomerGroupRequest request);

    /**
     * 移除分组客户
     *
     * @param request
     * @return
     */
    boolean removeGroupCustomers(RemoveGroupCustomersRequest request);

    /**
     * 移动分组客户
     *
     * @param request
     * @return
     */
    boolean moveGroupCustomers(MoveGroupCustomersRequest request);

    /**
     * 添加分组客户
     *
     * @param request
     * @return
     */
    boolean addGroupCustomers(SaveGroupCustomersRequest request);

    /**
     * 删除客户分组
     *
     * @param id 客户分组ID
     * @param opUserId 操作人ID
     * @return
     */
    boolean remove(Long id, Long opUserId);

    /**
     * 查询客户分组明细
     * @param customerGroupId   主键
     * @return
     */
    EnterpriseCustomerGroupDTO getById(Long customerGroupId);

    /**
     * 根据客户分组ID列表批量查询客户分组列表
     * @param customerGroupIds  客户分组ID列表
     * @return
     */
    List<EnterpriseCustomerGroupDTO> listByIds(List<Long> customerGroupIds);

    /**
     * 根据企业ID列表查询对应企业的分组数量
     * @param eids  企业ID列表
     * @return key:企业ID value:客户分组数量
     */
    Map<Long, Long> countCustomerGroupNumByEids(List<Long> eids);

    /**
     * 根据企业ID、分组名称查询对应企业的分组信息
     * @param eid
     * @param name
     * @return
     */
    EnterpriseCustomerGroupDTO getByEidAndName(Long eid, String name);

    /**
     * 根据企业ID查询客户分组列表
     * @param eid  企业ID
     * @return
     */
    List<EnterpriseCustomerGroupDTO> listByEid(Long eid);

}
