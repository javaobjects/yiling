package com.yiling.user.system.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.system.dto.request.UpdateIdCardInfoRequest;
import com.yiling.user.system.dto.request.UpdateUserRequest;
import com.yiling.user.system.dto.request.UpdateUserStatusRequest;
import com.yiling.user.system.entity.UserDO;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-18
 */
public interface UserService extends BaseService<UserDO> {

    /**
     * 修改用户信息
     *
     * @param request
     * @return
     */
    boolean update(UpdateUserRequest request);

    /**
     * 修改状态
     *
     * @param request
     * @return
     */
    boolean updateStatus(UpdateUserStatusRequest request);

    /**
     * 修改手机号
     *
     * @param userId 用户ID
     * @param appId 应用ID
     * @param mobile 新手机号
     * @param opUserId 操作人ID
     * @return
     */
    boolean changeMobile(Long userId, String appId, String mobile, Long opUserId);

    /**
     * 生成特殊段手机号码
     * @return
     */
    String generateSpecialPhone();

    /**
     * 校验是否为特殊号段
     * @param specialPhone
     * @return
     */
    boolean checkSpecialPhone(String specialPhone);

    /**
     * 更新身份证号及身份证附件
     *
     * @param request
     * @return
     */
    boolean updateIdCardInfo(UpdateIdCardInfoRequest request);

}
