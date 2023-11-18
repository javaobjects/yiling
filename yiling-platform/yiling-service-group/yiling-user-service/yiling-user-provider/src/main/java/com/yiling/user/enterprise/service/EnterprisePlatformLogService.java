package com.yiling.user.enterprise.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.user.enterprise.entity.EnterprisePlatformLogDO;
import com.yiling.user.enterprise.enums.EnterprisePlatformOpTypeEnum;

/**
 * <p>
 * 企业平台表操作日志 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-11-16
 */
public interface EnterprisePlatformLogService extends BaseService<EnterprisePlatformLogDO> {

    /**
     * 保存操作平台日志
     *
     * @param eid 企业ID
     * @param platformEnum 平台枚举
     * @param opType 操作类型枚举
     * @param opUserId 操作人ID
     * @return
     */
    boolean saveOpPlatformLog(Long eid, PlatformEnum platformEnum, EnterprisePlatformOpTypeEnum opType, Long opUserId);
}
