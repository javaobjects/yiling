package com.yiling.user.enterprise.api.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.CustomerContactApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerContactDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerContactRequest;
import com.yiling.user.enterprise.dto.request.ImportCustomerContactRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerContactPageListRequest;
import com.yiling.user.enterprise.dto.request.RemoveCustomerContactRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerContactDO;
import com.yiling.user.enterprise.service.EnterpriseCustomerContactService;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.service.UserService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

/**
 * 客户商务联系人 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/6/4
 */
@DubboService
public class CustomerContactApiImpl implements CustomerContactApi {

    @Autowired
    EnterpriseCustomerContactService enterpriseCustomerContactService;
    @Autowired
    EnterpriseEmployeeService enterpriseEmployeeService;
    @Autowired
    UserService userService;

    @Override
    public Page<EnterpriseCustomerContactDTO> pageList(QueryCustomerContactPageListRequest request) {
        Page<EnterpriseCustomerContactDO> page = enterpriseCustomerContactService.pageList(request);
        return PojoUtils.map(page, EnterpriseCustomerContactDTO.class);
    }

    @Override
    public boolean add(AddCustomerContactRequest request) {
        EnterpriseCustomerContactDO entity = PojoUtils.map(request, EnterpriseCustomerContactDO.class);
        return enterpriseCustomerContactService.save(entity);
    }

    @Override
    public boolean remove(RemoveCustomerContactRequest request) {
        QueryWrapper<EnterpriseCustomerContactDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerContactDO::getEid, request.getEid())
                .eq(EnterpriseCustomerContactDO::getCustomerEid, request.getCustomerEid())
                .eq(EnterpriseCustomerContactDO::getContactUserId, request.getContactUserId());

        EnterpriseCustomerContactDO entity = new EnterpriseCustomerContactDO();
        entity.setOpUserId(request.getOpUserId());

        return enterpriseCustomerContactService.batchDeleteWithFill(entity, queryWrapper) > 0;
    }

    @Override
    public List<EnterpriseCustomerContactDTO> listByEidAndCustomerEid(Long eid, Long customerEid) {
        return PojoUtils.map(enterpriseCustomerContactService.listByEidAndCustomerEid(eid, customerEid), EnterpriseCustomerContactDTO.class);
    }

    @Override
    public List<EnterpriseCustomerContactDTO> listByEidsAndCustomerEid(List<Long> eids, Long customerEid) {

        List<EnterpriseCustomerContactDO> resultList =  enterpriseCustomerContactService.listByEidsAndCustomerEid(eids,customerEid);

        return PojoUtils.map(resultList,EnterpriseCustomerContactDTO.class);
    }

    @Override
    public Map<Long, List<UserDTO>> listByEidAndCustomerEidList(Long eid, List<Long> customerEidList) {
        List<EnterpriseCustomerContactDO> customerContactDOList = enterpriseCustomerContactService.listByEidAndCustomerEidList(eid, customerEidList);
        Map<Long, List<EnterpriseCustomerContactDO>> customerMap = customerContactDOList.stream().collect(Collectors.groupingBy(EnterpriseCustomerContactDO::getCustomerEid));

        Map<Long, List<UserDTO>> userMap = MapUtil.newHashMap();
        customerMap.forEach((customerEid, enterpriseCustomerContactDOS) -> {
            List<Long> userIdList = enterpriseCustomerContactDOS.stream().map(EnterpriseCustomerContactDO::getContactUserId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(userIdList)) {
                List<UserDO> userDOList = userService.listByIds(userIdList);
                userMap.put(customerEid, PojoUtils.map(userDOList, UserDTO.class));
            }
        });

        return userMap;
    }

    @Override
    public boolean importCustomerContact(List<ImportCustomerContactRequest> customerContactRequestList) {
        return enterpriseCustomerContactService.importCustomerContact(customerContactRequestList);
    }
}
