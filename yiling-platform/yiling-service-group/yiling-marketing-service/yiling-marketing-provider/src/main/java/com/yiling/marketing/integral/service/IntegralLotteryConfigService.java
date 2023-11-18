package com.yiling.marketing.integral.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.integral.entity.IntegralLotteryConfigDO;
import com.yiling.user.integral.dto.IntegralLotteryConfigDTO;

/**
 * <p>
 * 积分参与抽奖活动配置表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
public interface IntegralLotteryConfigService extends BaseService<IntegralLotteryConfigDO> {

    /**
     * 根据消耗规则ID查询积分参与抽奖活动配置
     *
     * @param useRuleId
     * @return
     */
    IntegralLotteryConfigDTO getByUseRuleId(Long useRuleId);

}
