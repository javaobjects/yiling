package com.yiling.admin.b2b.lotteryactivity.form;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加抽奖活动-奖品设置 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddLotteryActivityRewardSettingForm extends BaseForm {

    /**
     * 等级：1-一等奖 2-二等奖 3-三等奖 (以此类推)
     */
    @NotNull
    @Range(min = 1, max = 8)
    @ApiModelProperty("等级：1-一等奖 2-二等奖 3-三等奖 (以此类推)")
    private Integer level;

    /**
     * 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会（见枚举：LotteryActivityRewardTypeEnum）
     */
    @NotNull
    @Range(min = 1, max = 10)
    @ApiModelProperty("奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会（见字典：lottery_activity_reward_type）")
    private Integer rewardType;

    /**
     * 关联奖品ID
     */
    @ApiModelProperty("关联奖品ID")
    private Long rewardId;

    /**
     * 奖品名称
     */
    @NotEmpty
    @ApiModelProperty("奖品名称")
    private String rewardName;

    /**
     * 展示名称
     */
    @Length(max = 10)
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
    @Length(max = 50)
    @ApiModelProperty("空奖提示语")
    private String emptyWords;

    /**
     * 奖品数量 （空奖的时候为空）
     */
    @Max(99)
    @ApiModelProperty("奖品数量")
    private Integer rewardNumber;

    /**
     * 每天最大抽中数量（0为不限制）
     */
    @NotNull
    @ApiModelProperty("每天最大抽中数量（0为不限制）")
    private Integer everyMaxNumber;

    /**
     * 中奖概率
     */
    @NotNull
    @ApiModelProperty("中奖概率")
    private BigDecimal hitProbability;

}
