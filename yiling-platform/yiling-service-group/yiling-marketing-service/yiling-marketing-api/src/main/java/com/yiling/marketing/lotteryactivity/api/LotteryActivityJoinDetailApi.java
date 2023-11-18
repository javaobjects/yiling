package com.yiling.marketing.lotteryactivity.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityExpressCashBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityJoinDetailBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinDetailDTO;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryActivityReceiptInfoRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailNumberRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateCashRewardRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateLotteryActivityReceiptInfoRequest;

/**
 * <p>
 * 抽奖活动参与/中奖明细 API
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-02
 */
public interface LotteryActivityJoinDetailApi {

    /**
     * 查询参与抽奖次数明细分页列表
     *
     * @param request
     * @return
     */
    Page<LotteryActivityJoinDetailDTO> queryJoinDetailPage(QueryJoinDetailPageRequest request);

    /**
     * 兑付奖品（运营后台兑付）
     *
     * @param request
     * @return
     */
    boolean cashReward(UpdateCashRewardRequest request);

    /**
     * 修改兑付信息
     *
     * @param receiptInfoRequest
     * @return
     */
    boolean updateCashInfo(UpdateLotteryActivityReceiptInfoRequest receiptInfoRequest);

    /**
     * 获取抽奖活动参与/中奖次数
     *
     * @param request
     * @return key为抽奖活动ID，value为对应的数量
     */
    Map<Long, Long> getJoinDetailNumber(QueryJoinDetailNumberRequest request);

    /**
     * 获取中奖名单
     *
     * @param lotteryActivityId
     * @param limit
     */
    List<LotteryActivityJoinDetailDTO> queryHitList(Long lotteryActivityId, Integer limit);

    /**
     * 添加收货地址信息 (C端)
     *
     * @param request
     * @return
     */
    boolean addReceiptInfo(UpdateLotteryActivityReceiptInfoRequest request);

    /**
     * 获取奖品详情
     *
     * @param joinDetailId
     * @return
     */
    LotteryActivityJoinDetailBO getRewardDetail(Long joinDetailId);

    /**
     * 查看快递信息或卡号
     *
     * @param joinDetailId
     * @return
     */
    LotteryActivityExpressCashBO getExpressOrCash(Long joinDetailId);

    /**
     * 修改收货地址信息
     *
     * @param request
     * @return
     */
    boolean updateReceiptInfo(UpdateLotteryActivityReceiptInfoRequest request);

    /**
     * 添加收货地址（B端）
     *
     * @param request
     */
    boolean addToBReceiptInfo(AddLotteryActivityReceiptInfoRequest request);

    /**
     * 生成虚拟中奖信息（定时任务每30分钟执行）
     *
     * @return
     */
    boolean generateVirtualHit();
}
