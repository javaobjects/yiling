package com.yiling.marketing.lotteryactivity.service;

import java.util.List;

import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRewardSettingDTO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGiveEnterpriseTypeDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 抽奖活动-赠送范围企业类型表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
public interface LotteryActivityGiveEnterpriseTypeService extends BaseService<LotteryActivityGiveEnterpriseTypeDO> {

    /**
     * 根据抽奖活动ID查询赠送企业类型
     *
     * @param lotteryActivityId
     * @return
     */
    List<Integer> getByLotteryActivityId(Long lotteryActivityId);

    /**
     * 根据抽奖活动ID保存or更新赠送企业类型
     *
     * @param lotteryActivityId
     * @param typeList
     * @param opUserId
     * @return
     */
    boolean updateGiveEnterpriseTypeByLotteryActivityId(Long lotteryActivityId, List<Integer> typeList, Long opUserId);

}
