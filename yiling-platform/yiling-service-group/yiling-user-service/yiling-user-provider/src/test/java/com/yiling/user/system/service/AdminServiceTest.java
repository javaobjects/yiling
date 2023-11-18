package com.yiling.user.system.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.user.BaseTest;
import com.yiling.user.system.dto.request.SaveAdminRequest;

/**
 * 管理员 Service Test
 *
 * @author: xuan.zhou
 * @date: 2021/5/18
 */
public class AdminServiceTest extends BaseTest {

    @Autowired
    AdminService adminService;

    @Test
    public void save() {
        SaveAdminRequest request = new SaveAdminRequest();
        request.setUsername("admin");
        request.setPassword("888888");
        request.setName("超级管理员");
        request.setGender(1);
        request.setMobile("13900000000");
        request.setEmail("xxx@email.com");
        request.setStatus(1);
        request.setOpUserId(0L);

        adminService.save(request);
    }
}
