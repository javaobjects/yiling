package com.yiling.user.system.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.system.api.AdminApi;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.dto.request.QueryAdminPageListRequest;
import com.yiling.user.system.dto.request.SaveAdminRequest;
import com.yiling.user.system.service.AdminService;

import lombok.extern.slf4j.Slf4j;

/**
 * 后台管理员 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/5/18
 */
@Slf4j
@DubboService
public class AdminApiImpl implements AdminApi {

    @Autowired
    AdminService adminService;

    @Override
    public Page<Admin> pageList(QueryAdminPageListRequest request) {
        return adminService.pageList(request);
    }

    @Override
    public Admin getById(Long id) {
        return adminService.getById(id);
    }

    @Override
    public Admin getByUsername(String username) {
        return adminService.getByUsername(username);
    }

    @Override
    public Admin getByMobile(String mobile) {
        return adminService.getByMobile(mobile);
    }

    @Override
    public Admin getByName(String name) {
        return adminService.getByName(name);
    }

    @Override
    public List<Admin> getListByName(String name, Integer limit) {
        return adminService.getListByName(name, limit);
    }

    @Override
    public boolean save(SaveAdminRequest request) {
        return adminService.save(request);
    }

    @Override
    public boolean updatePassword(Long id, String password, Long opUserId) {
        return adminService.updatePassword(id, password, opUserId);
    }

    @Override
    public boolean resetPassword(Long id, Long opUserId) {
        return adminService.resetPassword(id, opUserId);
    }

    @Override
    public boolean checkPassword(Long userId, String password) {
        return adminService.checkPassword(userId, password);
    }

}
