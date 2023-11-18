package com.yiling.admin.b2b.lotteryactivity.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动详情 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Data
@Accessors(chain = true)
public class LotteryActivityDetailVO {

    /**
     * 抽奖活动基础信息
     */
    @ApiModelProperty("抽奖活动基础信息")
    private LotteryActivityBasicVO lotteryActivityBasic;

    /**
     * 预算金额
     */
    @ApiModelProperty("预算金额")
    private BigDecimal budgetAmount;

    /**
     * B端赠送范围
     */
    @ApiModelProperty("B端赠送范围")
    private LotteryActivityGiveScopeVO activityGiveScope;

    /**
     * C端参与规则
     */
    @ApiModelProperty("C端参与规则")
    private LotteryActivityJoinRuleVO activityJoinRule;

    /**
     * 抽奖活动奖品设置集合
     */
    @ApiModelProperty("抽奖活动奖品设置集合")
    private List<LotteryActivityRewardSettingVO> activityRewardSettingList;

    /**
     * 活动规则说明
     */
    @ApiModelProperty("活动规则说明")
    private String content;

    /**
     * 活动背景图key
     */
    @ApiModelProperty("活动背景图key")
    private String bgPicture;

    /**
     * 活动背景图url
     */
    @ApiModelProperty("活动背景图url")
    private String bgPictureUrl;

}
