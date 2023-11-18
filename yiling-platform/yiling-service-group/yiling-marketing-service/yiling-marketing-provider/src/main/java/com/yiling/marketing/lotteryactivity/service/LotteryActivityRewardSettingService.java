package com.yiling.marketing.lotteryactivity.service;

import java.util.List;

import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRewardSettingDTO;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateRewardSettingRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityRewardSettingDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 抽奖活动-奖品设置表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
public interface LotteryActivityRewardSettingService extends BaseService<LotteryActivityRewardSettingDO> {

    /**
     * 根据抽奖活动ID查询奖品设置
     *
     * @param lotteryActivityId
     * @return
     */
    List<LotteryActivityRewardSettingDTO> getByLotteryActivityId(Long lotteryActivityId);

    /**
     * 修改奖品设置信息
     *
     * @param request
     * @return
     */
    boolean updateRewardSetting(UpdateRewardSettingRequest request);
}
