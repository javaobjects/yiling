package com.yiling.b2b.app.lotteryactivity.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动-奖品设置 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityRewardSettingVO extends BaseVO {

    /**
     * 等级：1-一等奖 2-二等奖 3-三等奖 (以此类推)
     */
    @ApiModelProperty("等级：1-一等奖 2-二等奖 3-三等奖 (以此类推，最多8个)")
    private Integer level;

    /**
     * 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会（见枚举：LotteryActivityRewardTypeEnum）
     */
    @ApiModelProperty("奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会（见字典：lottery_activity_reward_type）")
    private Integer rewardType;

    /**
     * 奖品名称
     */
    @ApiModelProperty("奖品名称")
    private String rewardName;

    /**
     * 展示名称
     */
    @ApiModelProperty("展示名称")
    private String showName;

    /**
     * 奖品图片
     */
    @ApiModelProperty("奖品图片")
    private String rewardImg;

    /**
     * 空奖提示语
     */
    @ApiModelProperty("空奖提示语")
    private String emptyWords;

    /**
     * 奖品数量
     */
    @ApiModelProperty("奖品数量")
    private Integer rewardNumber;

}
