package com.yiling.marketing.lotteryactivity.service;

import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRuleInstructionDTO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityRuleInstructionDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 活动规则说明表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
public interface LotteryActivityRuleInstructionService extends BaseService<LotteryActivityRuleInstructionDO> {

    /**
     * 根据抽奖活动ID查询抽奖规则说明
     *
     * @param lotteryActivityId
     * @return
     */
    LotteryActivityRuleInstructionDTO getByLotteryActivityId(Long lotteryActivityId);

}
