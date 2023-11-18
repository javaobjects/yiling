package com.yiling.marketing.lotteryactivity.service;

import com.yiling.marketing.lotteryactivity.dto.LotteryActivityGiveScopeDTO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGiveScopeDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 抽奖活动B端赠送范围表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-13
 */
public interface LotteryActivityGiveScopeService extends BaseService<LotteryActivityGiveScopeDO> {

    /**
     * 根据抽奖活动ID获取赠送范围信息
     *
     * @param lotteryActivityId
     * @return
     */
    LotteryActivityGiveScopeDTO getByLotteryActivityId(Long lotteryActivityId);

    /**
     * 根据抽奖活动ID进行删除规则
     *
     * @param lotteryActivityId
     * @param opUserId
     * @return
     */
    void deleteByLotteryActivityId(Long lotteryActivityId, Long opUserId);

}
