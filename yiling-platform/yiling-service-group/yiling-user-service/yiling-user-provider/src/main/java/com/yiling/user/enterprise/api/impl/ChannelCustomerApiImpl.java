package com.yiling.user.enterprise.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.ChannelCustomerApi;
import com.yiling.user.enterprise.bo.ChannelCustomerBO;
import com.yiling.user.enterprise.dto.EnterpriseChannelCustomerDTO;
import com.yiling.user.enterprise.dto.request.QueryChannelCustomerPageListRequest;
import com.yiling.user.enterprise.service.EnterpriseCustomerService;

/**
 * @author: yuecheng.chen
 * @date: 2021/6/4 0004
 */
@DubboService
public class ChannelCustomerApiImpl implements ChannelCustomerApi {

    @Autowired
    EnterpriseCustomerService enterpriseCustomerService;

    @Override
    public Page<EnterpriseChannelCustomerDTO> pageList(QueryChannelCustomerPageListRequest request) {
        Page<ChannelCustomerBO> page = enterpriseCustomerService.pageChannelCustomerList(request);
        return PojoUtils.map(page, EnterpriseChannelCustomerDTO.class);
    }

    @Override
    public List<EnterpriseChannelCustomerDTO> queryChannelCustomerList(QueryChannelCustomerPageListRequest request) {
        List<ChannelCustomerBO> list = enterpriseCustomerService.queryChannelCustomerList(request);
        return PojoUtils.map(list, EnterpriseChannelCustomerDTO.class);
    }

    @Override
    public EnterpriseChannelCustomerDTO get(Long eid, Long customerEid) {
        return PojoUtils.map(enterpriseCustomerService.getChannelCustomer(eid, customerEid), EnterpriseChannelCustomerDTO.class);
    }

    @Override
    public Map<Long, Long> countCustomersByEids(List<Long> eids) {
        return enterpriseCustomerService.countCustomersByEids(eids);
    }
}
