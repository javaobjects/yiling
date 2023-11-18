package com.yiling.user.web.auth.controller;

import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;

import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.IErrorCode;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserDeregisterAccountApi;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDeregisterAccountDTO;
import com.yiling.user.system.enums.UserDeregisterAccountStatusEnum;
import com.yiling.user.system.enums.UserStatusEnum;
import com.yiling.user.web.auth.enums.LoginErrorCode;

import cn.hutool.core.util.StrUtil;

/**
 * 认证基础 Controller
 *
 * @author: xuan.zhou
 * @date: 2022/4/7
 */
public class BaseAuthController extends BaseController {

    @DubboReference
    UserDeregisterAccountApi userDeregisterAccountApi;
    @DubboReference
    StaffApi staffApi;

    protected IErrorCode validateStaff(Staff staff, String inputPassword) {
        if (staff == null) {
            return LoginErrorCode.ACCOUNT_UNREGISTERED;
        }

        if (UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DISABLED) {
            return LoginErrorCode.ACCOUNT_DISABLED;
        }

        if (StrUtil.isNotEmpty(inputPassword)) {
            boolean result = staffApi.checkPassword(staff.getId(), inputPassword);
            if (!result) {
                return LoginErrorCode.ACCOUNT_OR_PASSWORD_ERROR;
            }
        }

        // 校验注销账号不可登录
        if (UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DEREGISTER) {
            return LoginErrorCode.ACCOUNT_HAD_DEREGISTER;
        }

        UserDeregisterAccountDTO deregisterAccountDTO = userDeregisterAccountApi.getByUserId(staff.getId());
        if (Objects.nonNull(deregisterAccountDTO) && deregisterAccountDTO.getStatus().equals(UserDeregisterAccountStatusEnum.WAITING_DEREGISTER.getCode()) ) {
            return LoginErrorCode.ACCOUNT_DEREGISTERING;
        }

        return null;
    }
}
