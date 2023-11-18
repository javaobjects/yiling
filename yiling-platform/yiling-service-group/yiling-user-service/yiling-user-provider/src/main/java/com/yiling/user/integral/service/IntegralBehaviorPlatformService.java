package com.yiling.user.integral.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.integral.entity.IntegralBehaviorPlatformDO;

/**
 * <p>
 * 积分行为适用平台表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-03
 */
public interface IntegralBehaviorPlatformService extends BaseService<IntegralBehaviorPlatformDO> {

    /**
     * 根据行为ID获取行为适用的平台
     *
     * @param behaviorId
     * @return
     */
    List<Integer> getByBehaviorId(Long behaviorId);

}
