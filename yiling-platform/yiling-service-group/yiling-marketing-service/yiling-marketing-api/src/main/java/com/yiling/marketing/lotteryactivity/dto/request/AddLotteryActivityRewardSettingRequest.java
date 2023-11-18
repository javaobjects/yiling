package com.yiling.marketing.lotteryactivity.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加抽奖活动-奖品设置 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddLotteryActivityRewardSettingRequest extends BaseRequest {

    /**
     * 等级：1-一等奖 2-二等奖 3-三等奖 (以此类推)
     */
    private Integer level;

    /**
     * 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会（见枚举：LotteryActivityRewardTypeEnum）
     */
    private Integer rewardType;

    /**
     * 关联奖品ID
     */
    private Long rewardId;

    /**
     * 奖品名称
     */
    private String rewardName;

    /**
     * 展示名称
     */
    private String showName;

    /**
     * 奖品图片
     */
    private String rewardImg;

    /**
     * 空奖提示语
     */
    private String emptyWords;

    /**
     * 奖品数量
     */
    private Integer rewardNumber;

    /**
     * 每天最大抽中数量（0为不限制）
     */
    private Integer everyMaxNumber;

    /**
     * 中奖概率
     */
    private BigDecimal hitProbability;

}
