package com.yiling.b2b.app.lotteryactivity.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动详情页 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-05
 */
@Data
@Accessors(chain = true)
public class LotteryActivityDetailPageVO extends BaseVO {

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 活动进度：1-未开始 2-进行中 3-已结束 （动态状态，枚举：lottery_activity_progress ，字典：LotteryActivityProgressEnums）
     */
    @ApiModelProperty("活动进度：1-未开始 2-进行中 3-已结束 （动态状态，字典：lottery_activity_progress）")
    private Integer progress;

    /**
     * 剩余抽奖次数
     */
    @ApiModelProperty("剩余抽奖次数")
    private Integer lotteryTimes;

    /**
     * 抽奖活动奖品设置集合
     */
    @ApiModelProperty("抽奖活动奖品设置集合")
    private List<LotteryActivityRewardSettingVO> activityRewardSettingList;

    /**
     * 中奖名单
     */
    @ApiModelProperty("中奖名单")
    private List<LotteryActivityHitVO> activityHitList;

    /**
     * 当前服务器时间
     */
    @ApiModelProperty("当前服务器时间")
    private Date currentTime;

    @ApiModelProperty("积分抽奖-用户剩余积分总数")
    private Integer integralValue;

    @ApiModelProperty("积分抽奖-积分参与抽奖活动-消耗积分值")
    private Integer useIntegralValue;

    @ApiModelProperty("积分抽奖-剩余兑换次数")
    private Integer exchangeTimesLimit;

    @ApiModelProperty("true 已配置积分  false 未配置积分")
    private Boolean isConfigIntegral;
}
