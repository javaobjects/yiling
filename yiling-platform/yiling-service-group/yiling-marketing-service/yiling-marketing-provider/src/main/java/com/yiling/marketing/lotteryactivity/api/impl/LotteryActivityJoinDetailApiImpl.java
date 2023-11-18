package com.yiling.marketing.lotteryactivity.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityJoinDetailApi;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityExpressCashBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityJoinDetailBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinDetailDTO;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryActivityReceiptInfoRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailNumberRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateCashRewardRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateLotteryActivityReceiptInfoRequest;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityJoinDetailService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityVirtualHitService;

/**
 * <p>
 * 抽奖活动参与/中奖明细 API实现
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-02
 */
@DubboService
public class LotteryActivityJoinDetailApiImpl implements LotteryActivityJoinDetailApi {

    @Autowired
    private LotteryActivityJoinDetailService lotteryActivityJoinDetailService;
    @Autowired
    private LotteryActivityVirtualHitService lotteryActivityVirtualHitService;

    @Override
    public Page<LotteryActivityJoinDetailDTO> queryJoinDetailPage(QueryJoinDetailPageRequest request) {
        return lotteryActivityJoinDetailService.queryJoinDetailPage(request);
    }

    @Override
    public boolean cashReward(UpdateCashRewardRequest request) {
        return lotteryActivityJoinDetailService.cashReward(request);
    }

    @Override
    public boolean updateCashInfo(UpdateLotteryActivityReceiptInfoRequest receiptInfoRequest) {
        return lotteryActivityJoinDetailService.updateCashInfo(receiptInfoRequest);
    }

    @Override
    public Map<Long, Long> getJoinDetailNumber(QueryJoinDetailNumberRequest request) {
        return lotteryActivityJoinDetailService.getJoinDetailNumber(request);
    }

    @Override
    public List<LotteryActivityJoinDetailDTO> queryHitList(Long lotteryActivityId, Integer limit) {
        return lotteryActivityJoinDetailService.queryHitList(lotteryActivityId, limit);
    }

    @Override
    public boolean addReceiptInfo(UpdateLotteryActivityReceiptInfoRequest request) {
        return lotteryActivityJoinDetailService.addReceiptInfo(request);
    }

    @Override
    public LotteryActivityJoinDetailBO getRewardDetail(Long joinDetailId) {
        return lotteryActivityJoinDetailService.getRewardDetail(joinDetailId);
    }

    @Override
    public LotteryActivityExpressCashBO getExpressOrCash(Long joinDetailId) {
        return lotteryActivityJoinDetailService.getExpressOrCash(joinDetailId);
    }

    @Override
    public boolean updateReceiptInfo(UpdateLotteryActivityReceiptInfoRequest request) {
        return lotteryActivityJoinDetailService.updateReceiptInfo(request);
    }

    @Override
    public boolean addToBReceiptInfo(AddLotteryActivityReceiptInfoRequest request) {
        return lotteryActivityJoinDetailService.addToBReceiptInfo(request);
    }

    @Override
    public boolean generateVirtualHit() {
        return lotteryActivityVirtualHitService.generateVirtualHit();
    }
}
