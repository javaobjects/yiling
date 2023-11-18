package com.yiling.marketing.lotteryactivity.bo;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖结果 BO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryResultBO extends BaseDTO {

    /**
     * 是否有抽奖次数
     */
    private Boolean lotteryTimesFlag;

    /**
     * 是否中奖
     */
    private Boolean rewardFlag;

    /**
     * 剩余抽奖次数
     */
    private Integer availableTimes;

    /**
     * 等级：1-一等奖 2-二等奖 3-三等奖 (以此类推)
     */
    private Integer level;

    /**
     * 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会 (见字典：lottery_activity_reward_type)
     */
    private Integer rewardType;

    /**
     * 奖品名称
     */
    private String rewardName;

    /**
     * 展示名称
     */
    private String showName;

    /**
     * 奖品数量
     */
    private Integer rewardNumber;

    /**
     * 奖品图片
     */
    private String rewardImg;

    /**
     * 空奖提示语
     */
    private String emptyWords;

    /**
     * 奖品在奖品列表的索引位置
     */
    private Integer rewardIndex;

}
