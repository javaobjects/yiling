package com.yiling.marketing.lotteryactivity.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityDetailBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityItemBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityRewardSettingBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRewardSettingDTO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityListRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveLotteryActivityBasicRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveLotteryActivitySettingRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityDO;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityPlatformEnum;

/**
 * <p>
 * 抽奖活动表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
public interface LotteryActivityService extends BaseService<LotteryActivityDO> {

    /**
     * 查询抽奖活动分页列表
     *
     * @param request
     * @return
     */
    Page<LotteryActivityItemBO> queryListPage(QueryLotteryActivityPageRequest request);

    /**
     * 查询抽奖活动列表
     *
     * @param request
     * @return
     */
    List<LotteryActivityDTO> queryList(QueryLotteryActivityListRequest request);

    /**
     * 获取抽奖活动详情
     *
     * @param id
     * @return
     */
    LotteryActivityDetailBO get(Long id, LotteryActivityPlatformEnum platformEnum);

    /**
     * 保存抽奖活动设置信息
     *
     * @param lotteryActivityRequest
     * @return
     */
    boolean saveActivitySetting(SaveLotteryActivitySettingRequest lotteryActivityRequest);

    /**
     * 获取所有抽奖活动名称列表
     *
     * @param limit
     * @return
     */
    List<String> getNameList(Integer limit);

    /**
     * 获取奖品剩余数量
     *
     * @param rewardSettingDTO
     */
    LotteryActivityRewardSettingBO getRewardRemainNumber(LotteryActivityRewardSettingDTO rewardSettingDTO);

    /**
     * 保存抽奖活动基本信息
     *
     * @param activityBasicRequest
     * @return
     */
    LotteryActivityDTO saveActivityBasic(SaveLotteryActivityBasicRequest activityBasicRequest);

    /**
     * 设置奖品图片地址
     *
     * @param rewardType
     * @return
     */
    String getRewardImg(Integer rewardType);

    /**
     * 复制抽奖活动
     *
     * @param id
     * @param opUserId
     * @return
     */
    Long copyLottery(Long id, Long opUserId);
}
