package com.yiling.user.system.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.system.entity.AppLoginInfoDO;

/**
 * <p>
 * 登录信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-11-26
 */
public interface AppLoginInfoService extends BaseService<AppLoginInfoDO> {

    /**
     * 获取最近的登录信息
     *
     * @param appId 应用ID
     * @param userId 用户ID
     * @return
     */
    AppLoginInfoDO getLatestLoginInfoByUserId(Long appId, Long userId);
}
