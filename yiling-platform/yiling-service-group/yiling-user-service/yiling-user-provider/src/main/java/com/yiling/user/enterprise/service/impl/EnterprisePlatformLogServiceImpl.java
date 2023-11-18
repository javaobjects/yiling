package com.yiling.user.enterprise.service.impl;

import org.springframework.stereotype.Service;

import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.user.enterprise.dao.EnterprisePlatformLogMapper;
import com.yiling.user.enterprise.entity.EnterprisePlatformLogDO;
import com.yiling.user.enterprise.enums.EnterprisePlatformOpTypeEnum;
import com.yiling.user.enterprise.service.EnterprisePlatformLogService;

/**
 * <p>
 * 企业平台表操作日志 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-11-16
 */
@Service
public class EnterprisePlatformLogServiceImpl extends BaseServiceImpl<EnterprisePlatformLogMapper, EnterprisePlatformLogDO> implements EnterprisePlatformLogService {

    @Override
    public boolean saveOpPlatformLog(Long eid, PlatformEnum platformEnum, EnterprisePlatformOpTypeEnum opType, Long opUserId) {
        EnterprisePlatformLogDO entity = new EnterprisePlatformLogDO();
        entity.setEid(eid);
        entity.setPlatform(platformEnum.getCode());
        entity.setType(opType.getCode());
        entity.setOpUserId(opUserId);
        return this.save(entity);
    }
}
