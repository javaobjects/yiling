package com.yiling.basic.version.service;

import java.util.List;

import com.yiling.basic.version.entity.AppInfoDO;
import com.yiling.basic.version.enums.AppChannelEnum;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 应用信息 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-27
 */
public interface AppInfoService extends BaseService<AppInfoDO> {

    /**
     * 根据渠道查询这个渠道的所有应用
     *
     * @param channelEnum 渠道
     * @return app应用信息
     */
    List<AppInfoDO> listAppInfoByChannelCode(AppChannelEnum channelEnum);

    List<AppInfoDO> listByIdList(List<Long> idList);
}
