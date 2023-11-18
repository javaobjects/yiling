package com.yiling.marketing.lotteryactivity.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityExpressCashBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityJoinDetailBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinDetailDTO;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryActivityJoinDetailRequest;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryActivityReceiptInfoRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailNumberRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailRewardCountRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateCashRewardRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateLotteryActivityReceiptInfoRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityJoinDetailDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 抽奖活动参与明细表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
public interface LotteryActivityJoinDetailService extends BaseService<LotteryActivityJoinDetailDO> {

    /**
     * 查询参与抽奖次数明细分页列表
     *
     * @param request
     * @return
     */
    Page<LotteryActivityJoinDetailDTO> queryJoinDetailPage(QueryJoinDetailPageRequest request);

    /**
     * 兑付奖品
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
     * 获取参与/中奖次数
     *
     * @param request
     * @return
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
     * 添加收货地址信息（C端）
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
     * 添加抽奖活动明细
     *
     * @param request
     * @return
     */
    Long addJoinDetail(AddLotteryActivityJoinDetailRequest request);

    /**
     * 查看快递信息或卡密
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
     * 添加收货地址信息（B端）
     *
     * @param request
     * @return
     */
    boolean addToBReceiptInfo(AddLotteryActivityReceiptInfoRequest request);

    /**
     * 获取当天抽奖活动奖品被抽中的数量（不包括空奖和抽奖机会）
     *
     * @param request
     * @return
     */
    Integer getCurrentRewardCount(QueryJoinDetailRewardCountRequest request);
}
