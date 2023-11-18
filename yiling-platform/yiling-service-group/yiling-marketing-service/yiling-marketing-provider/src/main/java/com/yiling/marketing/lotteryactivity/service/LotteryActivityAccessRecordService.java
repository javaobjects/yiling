package com.yiling.marketing.lotteryactivity.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityAccessRecordDO;

/**
 * <p>
 * 抽奖活动访问记录表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-16
 */
public interface LotteryActivityAccessRecordService extends BaseService<LotteryActivityAccessRecordDO> {

    /**
     * 校验当日是否访问，并每日赠送抽奖次数（C端使用）
     *
     * @param activityId
     * @param platformType
     * @param uid
     * @return
     */
    boolean checkTodayAccess(Long activityId, Integer platformType, Long uid);

    /**
     * 校验是否访问过活动，并在活动开始赠送抽奖次数（B端使用）
     *
     * @param activityId
     * @param platformType
     * @param uid
     * @return
     */
    boolean checkActivityAccess(Long activityId, Integer platformType, Long uid);

}
