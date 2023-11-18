package com.yiling.marketing.lotteryactivity.service;

import com.yiling.marketing.lotteryactivity.entity.LotteryActivitySignRecordDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 抽奖活动签到记录表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-15
 */
public interface LotteryActivitySignRecordService extends BaseService<LotteryActivitySignRecordDO> {

    /**
     * 校验当日是否已经签到
     *
     * @param activityId
     * @param platformType
     * @param uid
     * @return
     */
    boolean checkTodaySign(Long activityId, Integer platformType, Long uid);

}
