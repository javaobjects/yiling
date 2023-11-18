package com.yiling.marketing.lotteryactivity.service;

import com.yiling.marketing.lotteryactivity.entity.LotteryActivityVirtualHitDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 抽奖活动-虚拟中奖名单生成表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-10-31
 */
public interface LotteryActivityVirtualHitService extends BaseService<LotteryActivityVirtualHitDO> {

    /**
     * 生成虚拟中奖信息（定时任务每30分钟执行）
     *
     * @return
     */
    boolean generateVirtualHit();

}
