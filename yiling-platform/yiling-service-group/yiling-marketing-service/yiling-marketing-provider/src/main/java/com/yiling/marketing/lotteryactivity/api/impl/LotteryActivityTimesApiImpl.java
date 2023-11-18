package com.yiling.marketing.lotteryactivity.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.marketing.lotteryactivity.api.LotteryActivityTimesApi;
import com.yiling.marketing.lotteryactivity.bo.LotteryResultBO;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryTimesRequest;
import com.yiling.marketing.lotteryactivity.dto.request.ReduceLotteryTimesRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SignAddTimesRequest;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityTimesService;
import com.yiling.user.common.util.bean.In;

/**
 * <p>
 * 抽奖活动参与/中奖明细 API实现
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-02
 */
@DubboService
public class LotteryActivityTimesApiImpl implements LotteryActivityTimesApi {

    @Autowired
    private LotteryActivityTimesService lotteryActivityTimesService;

    @Override
    public Integer getAvailableTimes(Long lotteryActivityId, Integer platform, Long eid) {
        return lotteryActivityTimesService.getAvailableTimes(lotteryActivityId, platform, eid);
    }

    @Override
    public Integer addLotteryTimes(AddLotteryTimesRequest request) {
        return lotteryActivityTimesService.addLotteryTimes(request);
    }

    @Override
    public LotteryResultBO executeLottery(ReduceLotteryTimesRequest request) {
        return lotteryActivityTimesService.executeLottery(request);
    }

    @Override
    public Integer sign(SignAddTimesRequest request) {
        return lotteryActivityTimesService.sign(request);
    }

    @Override
    public boolean clearUserLotteryTimes() {
        return lotteryActivityTimesService.clearUserLotteryTimes();
    }

}
