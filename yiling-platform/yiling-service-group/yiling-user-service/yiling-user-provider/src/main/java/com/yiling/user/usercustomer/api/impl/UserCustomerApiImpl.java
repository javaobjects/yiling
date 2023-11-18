package com.yiling.user.usercustomer.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.system.enums.UserTypeEnum;
import com.yiling.user.usercustomer.api.UserCustomerApi;
import com.yiling.user.usercustomer.dto.UserCustomerDTO;
import com.yiling.user.usercustomer.dto.request.AddUserCustomerRequest;
import com.yiling.user.usercustomer.dto.request.QueryUserCustomerRequest;
import com.yiling.user.usercustomer.dto.request.UpdateCustomerStatusRequest;
import com.yiling.user.usercustomer.dto.request.UpdateUserCustomerRequest;
import com.yiling.user.usercustomer.service.UserCustomerService;

/**
 * 销售助手-客户管理API impl
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/17
 */
@DubboService
public class UserCustomerApiImpl implements UserCustomerApi {

    @Autowired
    UserCustomerService userCustomerService;

    @Override
    public boolean add(AddUserCustomerRequest request) {
        return userCustomerService.add(request);
    }

    @Override
    public Page<UserCustomerDTO> pageList(QueryUserCustomerRequest request) {
        return userCustomerService.pageList(request);
    }

    @Override
    public UserCustomerDTO getById(Long id) {
        return userCustomerService.getCustomerById(id);
    }

    @Override
    public boolean update(UpdateUserCustomerRequest request) {
        return userCustomerService.update(request);
    }

    @Override
    public List<UserCustomerDTO> queryCustomerList(QueryUserCustomerRequest request) {
        return userCustomerService.queryCustomerList(request);
    }

    @Override
    public boolean updateCustomerStatus(UpdateCustomerStatusRequest request) {
        return userCustomerService.updateCustomerStatus(request);
    }

    @Override
    public UserCustomerDTO getByUserAndCustomerEid(Long userId, Long customerEid) {
        return userCustomerService.getByUserAndCustomerEid(userId,customerEid);
    }

    @Override
    public UserCustomerDTO getByCustomerEid(Long customerEid) {
        return userCustomerService.getByCustomerEid(customerEid);
    }

    @Override
    public boolean checkUserCustomer(Long userId, Long customerEid) {
        return userCustomerService.checkUserCustomer(userId,customerEid);
    }

    @Override
    public boolean checkUserSaleArea(Long userId, Long eid, UserTypeEnum userType, String regionCode) {
        return userCustomerService.checkUserSaleArea(userId, eid, userType, regionCode);
    }


}
