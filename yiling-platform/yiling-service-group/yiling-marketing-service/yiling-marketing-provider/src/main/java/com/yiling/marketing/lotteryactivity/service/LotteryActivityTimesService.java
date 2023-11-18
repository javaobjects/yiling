package com.yiling.marketing.lotteryactivity.service;

import java.util.List;
import java.util.Map;

import com.yiling.marketing.lotteryactivity.bo.LotteryResultBO;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryTimesRequest;
import com.yiling.marketing.lotteryactivity.dto.request.ReduceLotteryTimesRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SignAddTimesRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityTimesDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 抽奖次数表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
public interface LotteryActivityTimesService extends BaseService<LotteryActivityTimesDO> {

    /**
     * 获取剩余抽奖次数
     *
     * @param lotteryActivityId
     * @param platform
     * @param uid
     * @return
     */
    Integer getAvailableTimes(Long lotteryActivityId, Integer platform, Long uid);

    /**
     * 批量获取抽奖次数
     *
     * @param lotteryActivityIdList
     * @return
     */
    Map<Long, Integer> getUseTimes(List<Long> lotteryActivityIdList);

    /**
     * 增加抽奖次数
     *
     * @param request 增加次数Request
     * @return 剩余抽奖次数
     */
    Integer addLotteryTimes(AddLotteryTimesRequest request);

    /**
     * 执行抽奖
     *
     * @param request
     * @return
     */
    LotteryResultBO executeLottery(ReduceLotteryTimesRequest request);

    /**
     * 签到送抽奖次数
     *
     * @param request
     * @return
     */
    Integer sign(SignAddTimesRequest request);

    /**
     * 每日0点清零C端用户的抽奖次数
     *
     * @return
     */
    boolean clearUserLotteryTimes();

}
