package com.yiling.marketing.lotteryactivity.api;

import com.yiling.marketing.lotteryactivity.bo.LotteryResultBO;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryTimesRequest;
import com.yiling.marketing.lotteryactivity.dto.request.ReduceLotteryTimesRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SignAddTimesRequest;

/**
 * <p>
 * 抽奖次数 API
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-07
 */
public interface LotteryActivityTimesApi {

    /**
     * 获取剩余抽奖次数
     *
     * @param lotteryActivityId
     * @param platform
     * @param eid
     * @return
     */
    Integer getAvailableTimes(Long lotteryActivityId, Integer platform, Long eid);

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
