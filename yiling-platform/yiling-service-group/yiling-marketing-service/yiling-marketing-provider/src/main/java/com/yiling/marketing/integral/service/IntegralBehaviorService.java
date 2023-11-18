package com.yiling.marketing.integral.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.integral.entity.IntegralBehaviorDO;
import com.yiling.user.integral.dto.IntegralBehaviorDTO;

/**
 * <p>
 * 积分行为表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
public interface IntegralBehaviorService extends BaseService<IntegralBehaviorDO> {

    /**
     * 根据行为名称获取行为
     *
     * @param name
     * @return
     */
    IntegralBehaviorDTO getByName(String name);

}
