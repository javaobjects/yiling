package com.yiling.marketing.lotteryactivity.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityGiveScopeDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinRuleDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRewardSettingDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动详情 BO
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Data
@Accessors(chain = true)
public class LotteryActivityDetailBO implements Serializable {

    /**
     * 抽奖活动基础信息
     */
    private LotteryActivityDTO lotteryActivityBasic;

    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;

    /**
     * B端赠送范围
     */
    private LotteryActivityGiveScopeBO activityGiveScope;

    /**
     * C端参与规则
     */
    private LotteryActivityJoinRuleDTO activityJoinRule;

    /**
     * 抽奖活动奖品设置集合
     */
    private List<LotteryActivityRewardSettingBO> activityRewardSettingList;

    /**
     * 活动规则说明
     */
    private String content;

    /**
     * 活动背景图
     */
    private String bgPicture;


}
