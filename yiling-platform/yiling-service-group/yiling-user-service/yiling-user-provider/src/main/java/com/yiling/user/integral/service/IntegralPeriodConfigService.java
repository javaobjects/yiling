package com.yiling.user.integral.service;

import java.util.List;

import com.yiling.user.integral.dto.IntegralPeriodConfigDTO;
import com.yiling.user.integral.entity.IntegralPeriodConfigDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 积分周期配置表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
public interface IntegralPeriodConfigService extends BaseService<IntegralPeriodConfigDO> {

    /**
     * 获取积分周期配置信息
     *
     * @param giveRuleId
     * @return
     */
    List<IntegralPeriodConfigDTO> getIntegralPeriodConfigList(Long giveRuleId);

}
