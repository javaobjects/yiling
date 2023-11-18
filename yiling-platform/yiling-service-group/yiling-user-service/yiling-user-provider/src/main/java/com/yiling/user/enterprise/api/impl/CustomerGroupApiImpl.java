package com.yiling.user.enterprise.api.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.CustomerGroupApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerGroupRequest;
import com.yiling.user.enterprise.dto.request.AddResultGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.MoveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerGroupPageListRequest;
import com.yiling.user.enterprise.dto.request.RemoveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.SaveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerGroupRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerDO;
import com.yiling.user.enterprise.entity.EnterpriseCustomerGroupDO;
import com.yiling.user.enterprise.service.EnterpriseCustomerGroupService;
import com.yiling.user.enterprise.service.EnterpriseCustomerService;

import cn.hutool.core.collection.CollUtil;

/**
 * 企业客户分组 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/5/24
 */
@DubboService
public class CustomerGroupApiImpl implements CustomerGroupApi {

    @Autowired
    EnterpriseCustomerService enterpriseCustomerService;
    @Autowired
    EnterpriseCustomerGroupService enterpriseCustomerGroupService;

    @Override
    public Page<EnterpriseCustomerGroupDTO> pageList(QueryCustomerGroupPageListRequest request) {
        Page<EnterpriseCustomerGroupDO> page = enterpriseCustomerGroupService.pageList(request);
        return PojoUtils.map(page, EnterpriseCustomerGroupDTO.class);
    }

    @Override
    public Long add(AddCustomerGroupRequest request) {
        return enterpriseCustomerGroupService.add(request);
    }

    @Override
    public boolean update(UpdateCustomerGroupRequest request) {
        return enterpriseCustomerGroupService.update(request);
    }

    @Override
    public boolean removeGroupCustomers(RemoveGroupCustomersRequest request) {
        return enterpriseCustomerService.removeGroupCustomers(request);
    }

    @Override
    public boolean moveGroupCustomers(MoveGroupCustomersRequest request) {
        return enterpriseCustomerService.moveGroupCustomers(request);
    }

    @Override
    public boolean addGroupCustomers(SaveGroupCustomersRequest request) {
        return enterpriseCustomerService.addGroupCustomers(request);
    }

    @Override
    public boolean remove(Long id, Long opUserId) {
        //根据id查询出企业客户分组
        EnterpriseCustomerGroupDTO customerGroupDTO = Optional.ofNullable(this.getById(id))
                .orElseThrow(()->new BusinessException(UserErrorCode.CUSTOMER_GROUP_NOT_EXISTS));

        EnterpriseCustomerGroupDO entity = new EnterpriseCustomerGroupDO();
        entity.setId(id);
        entity.setOpUserId(opUserId);

        //删除组对应的客户信息
        List<Long> customerIdList = enterpriseCustomerService.listByEid(customerGroupDTO.getEid())
                .stream().filter(enterpriseCustomerDO -> enterpriseCustomerDO.getCustomerGroupId().compareTo(id) == 0)
                .map(EnterpriseCustomerDO::getCustomerEid).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(customerIdList)){
            RemoveGroupCustomersRequest request = new RemoveGroupCustomersRequest();
            request.setOpUserId(opUserId);
            request.setEid(customerGroupDTO.getEid());
            request.setCustomerEids(customerIdList);
            this.removeGroupCustomers(request);
        }

        return enterpriseCustomerGroupService.deleteByIdWithFill(entity) > 0;
    }

    @Override
    public EnterpriseCustomerGroupDTO getById(Long customerGroupId) {
        return PojoUtils.map(enterpriseCustomerGroupService.getById(customerGroupId), EnterpriseCustomerGroupDTO.class);
    }

    @Override
    public List<EnterpriseCustomerGroupDTO> listByIds(List<Long> customerGroupIds) {
        return PojoUtils.map(enterpriseCustomerGroupService.listByIds(customerGroupIds), EnterpriseCustomerGroupDTO.class);
    }

    @Override
    public Map<Long, Long> countCustomerGroupNumByEids(List<Long> eids) {
        return enterpriseCustomerGroupService.countCustomerGroupNumByEids(eids);
    }

    @Override
    public EnterpriseCustomerGroupDTO getByEidAndName(Long eid, String name) {
        return enterpriseCustomerGroupService.getByEidAndName(eid, name);
    }

    @Override
    public List<EnterpriseCustomerGroupDTO> listByEid(Long eid) {
        return PojoUtils.map(enterpriseCustomerGroupService.listByEid(eid),EnterpriseCustomerGroupDTO.class);
    }

}
