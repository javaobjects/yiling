package com.yiling.mall.userderegister.api;

import java.util.List;

import com.yiling.user.system.dto.UserDeregisterAssistantValidDTO;
import com.yiling.user.system.dto.UserDeregisterValidDTO;
import com.yiling.user.system.enums.UserTypeEnum;

/**
 * <p>
 * 用户注销账号 API
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-27
 */
public interface UserDeregisterAccountMallApi {

    /**
     * 校验注销账号
     *
     * @param userId
     * @return
     */
    List<UserDeregisterValidDTO> checkLogoutAccount(Long userId);

    /**
     * 销售助手校验注销账号
     *
     * @param userId
     * @param userTypeEnum
     * @return
     */
    List<UserDeregisterAssistantValidDTO> checkAssistantLogoutAccount(Long userId, Long eid, UserTypeEnum userTypeEnum);

    /**
     * 医药代表校验注销账号
     *
     * @param userId
     * @param userTypeEnum
     * @return
     */
    List<UserDeregisterAssistantValidDTO> checkMrLogoutAccount(Long userId, Long eid, UserTypeEnum userTypeEnum);

}
