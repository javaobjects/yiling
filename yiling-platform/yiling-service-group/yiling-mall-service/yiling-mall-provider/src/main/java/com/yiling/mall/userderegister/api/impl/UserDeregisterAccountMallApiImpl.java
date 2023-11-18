package com.yiling.mall.userderegister.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.mall.userderegister.api.UserDeregisterAccountMallApi;
import com.yiling.mall.userderegister.service.UserDeregisterAccountService;
import com.yiling.user.system.dto.UserDeregisterAssistantValidDTO;
import com.yiling.user.system.dto.UserDeregisterValidDTO;
import com.yiling.user.system.enums.UserTypeEnum;

/**
 * <p>
 * 用户注销账号 API实现
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-27
 */
@DubboService
public class UserDeregisterAccountMallApiImpl implements UserDeregisterAccountMallApi {

    @Autowired
    UserDeregisterAccountService userDeregisterAccountService;

    @Override
    public List<UserDeregisterValidDTO> checkLogoutAccount(Long userId) {
        return userDeregisterAccountService.checkLogoutAccount(userId);
    }

    @Override
    public List<UserDeregisterAssistantValidDTO> checkAssistantLogoutAccount(Long userId, Long eid, UserTypeEnum userTypeEnum) {
        return userDeregisterAccountService.checkAssistantLogoutAccount(userId, eid, userTypeEnum);
    }

    @Override
    public List<UserDeregisterAssistantValidDTO> checkMrLogoutAccount(Long userId, Long eid, UserTypeEnum userTypeEnum) {
        return userDeregisterAccountService.checkMrLogoutAccount(userId, eid, userTypeEnum);
    }

}
