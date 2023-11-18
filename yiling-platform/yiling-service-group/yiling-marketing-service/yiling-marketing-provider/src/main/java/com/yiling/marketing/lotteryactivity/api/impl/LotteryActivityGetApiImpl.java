package com.yiling.marketing.lotteryactivity.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityGetApi;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityGetDTO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityGetCountRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityGetPageRequest;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGetService;

/**
 * <p>
 * 获取抽奖机会明细 API实现
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-14
 */
@DubboService
public class LotteryActivityGetApiImpl implements LotteryActivityGetApi {

    @Autowired
    LotteryActivityGetService lotteryActivityGetService;

    @Override
    public Page<LotteryActivityGetDTO> queryPageList(QueryLotteryActivityGetPageRequest request) {
        return lotteryActivityGetService.queryPageList(request);
    }

    @Override
    public Map<Long, Integer> getNumberByLotteryActivityId(List<Long> lotteryActivityIdList) {
        return lotteryActivityGetService.getNumberByLotteryActivityId(lotteryActivityIdList);
    }

    @Override
    public Integer countByUidAndGetType(QueryLotteryActivityGetCountRequest request) {
        return lotteryActivityGetService.countByUidAndGetType(request);
    }
}
