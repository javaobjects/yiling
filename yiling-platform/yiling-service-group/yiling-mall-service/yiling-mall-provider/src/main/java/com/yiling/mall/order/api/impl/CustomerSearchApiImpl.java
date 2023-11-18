package com.yiling.mall.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.util.Assert;

import com.yiling.mall.customer.api.CustomerSearchApi;
import com.yiling.mall.customer.dto.request.CustomerVerificationRequest;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.common.enums.UserCustomerStatusEnum;
import com.yiling.user.enterprise.api.CustomerContactApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerContactDTO;
import com.yiling.user.usercustomer.api.UserCustomerApi;
import com.yiling.user.usercustomer.dto.UserCustomerDTO;
import com.yiling.user.usercustomer.dto.request.QueryUserCustomerRequest;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户选择
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.order.api.impl
 * @date: 2021/9/15
 */
@DubboService
@Slf4j
public class CustomerSearchApiImpl implements CustomerSearchApi {
    @DubboReference
    UserCustomerApi userCustomerApi;
    @DubboReference
    CustomerContactApi customerContactApi;

    @Override
    public Boolean checkIsMyCustomer(CustomerVerificationRequest request) {
        Assert.notNull(request.getContactUserId(), "商务联系ID不能为空");
        Assert.notNull(request.getOrderType(), "类型不能为空");
        Assert.notNull(request.getCustomerEid(), "客户EID不能为空");

        if (OrderTypeEnum.POP == OrderTypeEnum.getByCode(request.getOrderType())) {
            List<EnterpriseCustomerContactDTO> customerContactDTOList =  customerContactApi.listByEidAndCustomerEid(request.getCurrentEid(),request.getCustomerEid());
            if (CollectionUtil.isEmpty(customerContactDTOList)) {
                return false;
            }
            return customerContactDTOList.stream().anyMatch(t -> request.getContactUserId().equals(t.getContactUserId()));
        } else if (OrderTypeEnum.B2B == OrderTypeEnum.getByCode(request.getOrderType())) {
            QueryUserCustomerRequest userCustomerRequest = new QueryUserCustomerRequest();
            userCustomerRequest.setUserId(request.getContactUserId());
            userCustomerRequest.setCustomerEid(request.getCustomerEid());
            userCustomerRequest.setEid(request.getCurrentEid());
            userCustomerRequest.setStatus(UserCustomerStatusEnum.PASS.getCode());
            List<UserCustomerDTO> userCustomerDTOList =  userCustomerApi.queryCustomerList(userCustomerRequest);
            if (CollectionUtil.isEmpty(userCustomerDTOList)) {
                return false;
            }
        }
        return true;
    }
}
