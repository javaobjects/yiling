package com.yiling.user.enterprise.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerGroupRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerGroupPageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerGroupRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerGroupDO;

/**
 * <p>
 * 企业客户分组 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-21
 */
public interface EnterpriseCustomerGroupService extends BaseService<EnterpriseCustomerGroupDO> {

    /**
     * 客户分组分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseCustomerGroupDO> pageList(QueryCustomerGroupPageListRequest request);

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
     * @param eid
     * @return
     */
    List<EnterpriseCustomerGroupDO> listByEid(Long eid);
}
