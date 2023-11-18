package com.yiling.user.integral.service;

import com.yiling.user.integral.dto.GenerateMultipleConfigDTO;
import com.yiling.user.integral.dto.IntegralOrderGiveConfigDTO;
import com.yiling.user.integral.dto.IntegralOrderGiveMultipleConfigDTO;
import com.yiling.user.integral.dto.request.QueryIntegralGiveMatchRuleRequest;
import com.yiling.user.integral.dto.request.SaveOrderGiveIntegralRequest;
import com.yiling.user.integral.entity.IntegralOrderGiveConfigDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 订单送积分配置表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
public interface IntegralOrderGiveConfigService extends BaseService<IntegralOrderGiveConfigDO> {

    /**
     * 根据发放规则ID获取订单送积分配置
     *
     * @param giveRuleId
     * @return
     */
    IntegralOrderGiveConfigDTO getByGiveRuleId(Long giveRuleId);

    /**
     * 保存订单送积分配置
     *
     * @param request
     * @return
     */
    boolean saveOrderGiveIntegral(SaveOrderGiveIntegralRequest request);

    /**
     * 自动匹配订单送积分的倍数规则
     *
     * @param request
     * @return
     */
    GenerateMultipleConfigDTO autoMatchRule(QueryIntegralGiveMatchRuleRequest request);

}
