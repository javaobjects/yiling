package com.yiling.hmc.lotteryactivity.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动我的奖品 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityMyRewardVO extends BaseVO {

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 抽奖时间
     */
    @ApiModelProperty("抽奖时间")
    private Date lotteryTime;

    /**
     * 等级：1-一等奖 2-二等奖 3-三等奖 (以此类推)
     */
    @ApiModelProperty("等级：1-一等奖 2-二等奖 3-三等奖 (以此类推)")
    private Integer level;

    /**
     * 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会 (见字典：lottery_activity_reward_type)
     */
    @ApiModelProperty("奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会 (见字典：lottery_activity_reward_type)")
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
     * 奖品数量
     */
    @ApiModelProperty("奖品数量")
    private Integer rewardNumber;

    /**
     * 兑付状态：1-已兑付 2-未兑付
     */
    @ApiModelProperty("兑付状态：1-已兑付 2-未兑付")
    private Integer status;

    /**
     * 是否展示选择收货地址按钮
     */
    @ApiModelProperty("是否展示选择收货地址按钮")
    private Boolean showAcceptFlag;

    /**
     * 是否展示修改收货地址按钮
     */
    @ApiModelProperty("是否展示修改收货地址按钮")
    private Boolean showUpdateAcceptFlag;

    /**
     * 是否展示查看快递信息按钮：真实物品才展示查看快递信息按钮
     */
    @ApiModelProperty("是否展示查看快递信息按钮")
    private Boolean showExpressFlag;

    /**
     * 是否展示查看兑换码按钮：虚拟物品才展示查看兑换码按钮
     */
    @ApiModelProperty("是否展示查看兑换码按钮")
    private Boolean showCardFlag;

}
