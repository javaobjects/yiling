package com.yiling.marketing.lotteryactivity.service;

import java.util.List;

import com.yiling.marketing.lotteryactivity.dto.LotteryActivityHitRandomGenerateDTO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityHitRandomGenerateDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 抽奖活动随机生成中奖名单表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-15
 */
public interface LotteryActivityHitRandomGenerateService extends BaseService<LotteryActivityHitRandomGenerateDO> {

    /**
     * 随机生成中奖名单（C端每小时执行一次）
     */
    void generateHitTask();

    /**
     * 获取当前时段随机生成的中奖用户
     *
     * @param activityId
     * @return
     */
    List<LotteryActivityHitRandomGenerateDTO> getCurrentGenerate(Long activityId);

}
