package com.yiling.user.system.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.user.system.api.SjmsUserApi;
import com.yiling.user.system.bo.SjmsUser;
import com.yiling.user.system.dto.request.CreateSjmsUserRequest;
import com.yiling.user.system.service.SjmsUserService;

/**
 * 神机妙算用户 API 实现
 *
 * @author: xuan.zhou
 * @date: 2022/11/24
 */
@DubboService
public class SjmsUserApiImpl implements SjmsUserApi {

    @Autowired
    SjmsUserService sjmsUserService;

    @Override
    public SjmsUser getById(Long id) {
        return sjmsUserService.getById(id);
    }

    @Override
    public List<SjmsUser> listByIds(List<Long> ids) {
        return sjmsUserService.listByIds(ids);
    }

    @Override
    public SjmsUser getByEmpId(String empId) {
        return sjmsUserService.getByEmpId(empId);
    }

    @Override
    public Long create(CreateSjmsUserRequest request) {
        return sjmsUserService.create(request);
    }
}
