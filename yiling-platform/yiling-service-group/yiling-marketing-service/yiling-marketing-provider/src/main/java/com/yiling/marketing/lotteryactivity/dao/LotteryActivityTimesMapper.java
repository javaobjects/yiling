package com.yiling.marketing.lotteryactivity.dao;

import com.yiling.marketing.lotteryactivity.entity.LotteryActivityTimesDO;
import com.yiling.framework.common.base.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 抽奖次数表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Repository
public interface LotteryActivityTimesMapper extends BaseMapper<LotteryActivityTimesDO> {

    /**
     * 每日0点清除C端抽奖次数
     *
     * @return
     */
    void clearLotteryTimes();

}
