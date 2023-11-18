package com.yiling.user.system.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.api.UserDeregisterAccountApi;
import com.yiling.user.system.dto.UserDeregisterAccountDTO;
import com.yiling.user.system.dto.request.AddDeregisterAccountRequest;
import com.yiling.user.system.dto.request.QueryUserDeregisterPageListRequest;
import com.yiling.user.system.dto.request.UpdateUserDeregisterRequest;
import com.yiling.user.system.service.UserDeregisterAccountService;

/**
 * <p>
 * 用户注销账号 API实现
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-06
 */
@DubboService
public class UserDeregisterAccountApiImpl implements UserDeregisterAccountApi {

    @Autowired
    UserDeregisterAccountService userDeregisterAccountService;

    @Override
    public boolean applyDeregisterAccount(AddDeregisterAccountRequest request) {
        return userDeregisterAccountService.applyDeregisterAccount(request);
    }

    @Override
    public boolean deregisterAccountTask() {
        return userDeregisterAccountService.deregisterAccountTask();
    }

    @Override
    public boolean deregisterAccountAuth(UpdateUserDeregisterRequest request) {
        return userDeregisterAccountService.deregisterAccountAuth(request);
    }

    @Override
    public UserDeregisterAccountDTO getByUserId(Long userId) {
        return PojoUtils.map(userDeregisterAccountService.getByUserId(userId), UserDeregisterAccountDTO.class);
    }

    @Override
    public Page<UserDeregisterAccountDTO> queryPageList(QueryUserDeregisterPageListRequest request) {
        return userDeregisterAccountService.queryPageList(request);
    }

    @Override
    public UserDeregisterAccountDTO getById(Long id) {
        return PojoUtils.map(userDeregisterAccountService.getById(id), UserDeregisterAccountDTO.class);
    }
}
