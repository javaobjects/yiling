package com.yiling.marketing.lotteryactivity.service;

import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinRuleDTO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityJoinRuleDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 抽奖活动-C端参与规则 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-13
 */
public interface LotteryActivityJoinRuleService extends BaseService<LotteryActivityJoinRuleDO> {

    /**
     * 根据抽奖活动ID获取C端参与规则
     *
     * @param lotteryActivityId
     * @return
     */
    LotteryActivityJoinRuleDTO getByLotteryActivityId(Long lotteryActivityId);

    /**
     * 根据抽奖活动ID进行删除规则
     *
     * @param lotteryActivityId
     * @param opUserId
     * @return
     */
    void deleteByLotteryActivityId(Long lotteryActivityId, Long opUserId);

}
