package com.yiling.marketing.lotteryactivity.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityDetailBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityItemBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityHitRandomGenerateDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinRuleDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRuleDTO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityRulePageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveLotteryActivityBasicRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveLotteryActivitySettingRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateRewardSettingRequest;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityPlatformEnum;

/**
 * <p>
 * 抽奖活动 API
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
public interface LotteryActivityApi {

    /**
     * 查询抽奖活动分页列表
     *
     * @param request
     * @return
     */
    Page<LotteryActivityItemBO> queryListPage(QueryLotteryActivityPageRequest request);

    /**
     * 获取抽奖活动详情
     *
     * @param id
     * @return
     */
    LotteryActivityDetailBO get(Long id, LotteryActivityPlatformEnum platformEnum);

    /**
     * 获取抽奖活动基础信息
     *
     * @param id
     * @return
     */
    LotteryActivityDTO getById(Long id);

    /**
     * 停用活动
     *
     * @param id
     * @param opUserId
     * @return
     */
    boolean stopActivity(Long id, Long opUserId);

    /**
     * 获取所有抽奖活动名称列表
     *
     * @return
     */
    List<String> getNameList(Integer limit);

    /**
     * 保存抽奖活动基本信息
     *
     * @param activityBasicRequest
     * @return
     */
    LotteryActivityDTO saveActivityBasic(SaveLotteryActivityBasicRequest activityBasicRequest);

    /**
     * 保存抽奖活动设置信息
     *
     * @param activitySettingRequest
     * @return
     */
    boolean saveActivitySetting(SaveLotteryActivitySettingRequest activitySettingRequest);

    /**
     * 查询抽奖活动规则分页列表
     *
     * @param request
     * @return
     */
    Page<LotteryActivityRuleDTO> queryRulePage(QueryLotteryActivityRulePageRequest request);

    /**
     * 校验当日是否已经签到  (C端使用)
     *
     * @param activityId
     * @param platformType
     * @param uid
     * @return
     */
    boolean checkTodaySign(Long activityId, Integer platformType, Long uid);

    /**
     * 校验当日是否访问，并每日赠送抽奖次数（C端使用）
     *
     * @param activityId
     * @param platformType
     * @param uid
     * @return
     */
    boolean checkTodayAccess(Long activityId, Integer platformType, Long uid);

    /**
     * 校验是否访问过活动，并在活动开始赠送抽奖次数（B端使用）
     *
     * @param activityId
     * @param platformType
     * @param uid
     * @return
     */
    boolean checkActivityAccess(Long activityId, Integer platformType, Long uid);

    /**
     * 获取C端活动参与规则
     *
     * @param activityId
     * @return
     */
    LotteryActivityJoinRuleDTO getByLotteryActivityId(Long activityId);

    /**
     * 获取当前时段随机生成的中奖用户 (C端使用)
     *
     * @param activityId
     * @return
     */
    List<LotteryActivityHitRandomGenerateDTO> getCurrentGenerate(Long activityId);

    /**
     * 随机生成中奖名单 (C端使用)
     */
    void generateHitTask();

    /**
     * 复制抽奖活动
     *
     * @param id
     * @param opUserId
     * @return
     */
    Long copyLottery(Long id, Long opUserId);

    /**
     * 修改奖品设置信息
     *
     * @param request
     * @return
     */
    boolean updateRewardSetting(UpdateRewardSettingRequest request);
}
