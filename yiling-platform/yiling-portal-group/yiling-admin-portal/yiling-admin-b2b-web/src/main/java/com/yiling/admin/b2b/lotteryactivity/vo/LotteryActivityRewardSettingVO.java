package com.yiling.admin.b2b.lotteryactivity.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
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
 * @date 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityRewardSettingVO extends BaseVO {

    /**
     * 等级：1-一等奖 2-二等奖 3-三等奖 (以此类推)
     */
    @ApiModelProperty("等级：1-一等奖 2-二等奖 3-三等奖 (以此类推)")
    private Integer level;

    /**
     * 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会（见枚举：LotteryActivityRewardTypeEnum）
     */
    @ApiModelProperty("奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会（见字典：lottery_activity_reward_type）")
    private Integer rewardType;

    /**
     * 关联奖品ID
     */
    private Long rewardId;

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
     * 剩余数量
     */
    @ApiModelProperty("剩余数量")
    private Integer remainNumber;

    /**
     * 奖品数量
     */
    @ApiModelProperty("奖品数量")
    private Integer rewardNumber;

    /**
     * 每天最大抽中数量（0为不限制）
     */
    @ApiModelProperty("每天最大抽中数量（0为不限制）")
    private Integer everyMaxNumber;

    /**
     * 中奖概率
     */
    @ApiModelProperty("中奖概率")
    private BigDecimal hitProbability;

}
