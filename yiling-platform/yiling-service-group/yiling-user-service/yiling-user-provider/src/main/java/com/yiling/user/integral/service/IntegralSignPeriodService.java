package com.yiling.user.integral.service;

import com.yiling.user.integral.dto.request.SaveSignPeriodRequest;
import com.yiling.user.integral.entity.IntegralSignPeriodDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 积分签到周期表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
public interface IntegralSignPeriodService extends BaseService<IntegralSignPeriodDO> {

    /**
     * 保存签到周期
     *
     * @param request
     * @return
     */
    boolean saveSignPeriod(SaveSignPeriodRequest request);

    /**
     * 获取签到周期
     *
     * @param giveRuleId
     * @return
     */
    Integer getSignPeriod(Long giveRuleId);

    /**
     * 获取签到周期对象
     *
     * @param giveRuleId
     * @return
     */
    IntegralSignPeriodDO getIntegralSignPeriod(Long giveRuleId);

}
