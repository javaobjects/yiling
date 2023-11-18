package com.yiling.marketing.lotteryactivity.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存抽奖活动设置信息 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveLotteryActivitySettingRequest extends BaseRequest {

    /**
     * 抽奖活动ID
     */
    private Long lotteryActivityId;

    /**
     * 抽奖活动赠送范围（B端使用规则）
     */
    private SaveLotteryActivityGiveScopeRequest activityGiveScope;

    /**
     * C端参与规则（C端使用规则）
     */
    private SaveLotteryActivityJoinRuleRequest activityJoinRule;

    /**
     * 抽奖活动奖品设置集合
     */
    private List<AddLotteryActivityRewardSettingRequest> activityRewardSettingList;

    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;

    /**
     * 活动规则说明
     */
    private String content;

    /**
     * 分享背景图
     */
    private String bgPicture;

}
