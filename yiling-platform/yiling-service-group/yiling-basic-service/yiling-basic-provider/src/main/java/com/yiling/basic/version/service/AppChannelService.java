package com.yiling.basic.version.service;

import java.util.List;

import com.yiling.basic.version.entity.AppChannelDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 应用渠道信息 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-27
 */
public interface AppChannelService extends BaseService<AppChannelDO> {

    /**
     * 根据渠道号好的渠道信息
     *
     * @param code
     * @return
     */
    List<AppChannelDO> queryByCode(String code);
}
