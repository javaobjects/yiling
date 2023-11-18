package com.yiling.mall.userderegister.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.userderegister.entity.UserDeregisterAccountDO;
import com.yiling.user.system.dto.UserDeregisterAssistantValidDTO;
import com.yiling.user.system.dto.UserDeregisterValidDTO;
import com.yiling.user.system.enums.UserTypeEnum;

/**
 * <p>
 * 注销账号表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-06
 */
public interface UserDeregisterAccountService extends BaseService<UserDeregisterAccountDO> {

    /**
     * 校验注销账号
     *
     * @param userId
     * @return
     */
    List<UserDeregisterValidDTO> checkLogoutAccount(Long userId);

    /**
     * 校验销售助手注销账号
     *
     * @param userId
     * @param eid
     * @param userTypeEnum
     * @return
     */
    List<UserDeregisterAssistantValidDTO> checkAssistantLogoutAccount(Long userId, Long eid, UserTypeEnum userTypeEnum);

    /**
     * 校验医药代表注销账号
     *
     * @param userId
     * @param eid
     * @param userTypeEnum
     * @return
     */
    List<UserDeregisterAssistantValidDTO> checkMrLogoutAccount(Long userId, Long eid, UserTypeEnum userTypeEnum);
}
