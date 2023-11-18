package com.yiling.user.system.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.request.CreateStaffRequest;
import com.yiling.user.system.dto.request.QueryStaffListRequest;
import com.yiling.user.system.dto.request.QueryStaffPageListRequest;
import com.yiling.user.system.dto.request.UpdateUserStatusRequest;
import com.yiling.user.system.service.StaffService;

/**
 * 员工 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/5/18
 */
@DubboService
public class StaffApiImpl implements StaffApi {

    @Autowired
    StaffService staffService;

    @Override
    public Staff getById(Long id) {
        return staffService.getById(id);
    }

    @Override
    public Staff getByUsername(String username) {
        return staffService.getByUsername(username);
    }

    @Override
    public Staff getByMobile(String mobile) {
        return staffService.getByMobile(mobile);
    }

    @Override
    public Page<Staff> pageList(QueryStaffPageListRequest request) {
        return staffService.pageList(request);
    }

    @Override
    public List<Staff> list(QueryStaffListRequest request) {
        return staffService.list(request);
    }

    @Override
    public boolean updatePassword(Long id, String password, Long opUserId) {
        return staffService.updatePassword(id, password, opUserId);
    }

    @Override
    public boolean resetPassword(Long id, Long opUserId) {
        return staffService.resetPassword(id, opUserId);
    }

    @Override
    public boolean checkPassword(Long userId, String password) {
        return staffService.checkPassword(userId, password);
    }

    @Override
    public Long create(CreateStaffRequest request) {
        return staffService.create(request);
    }
}
