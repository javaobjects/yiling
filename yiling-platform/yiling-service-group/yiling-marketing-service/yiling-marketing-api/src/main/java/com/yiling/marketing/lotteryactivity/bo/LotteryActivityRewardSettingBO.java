package com.yiling.marketing.lotteryactivity.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRewardSettingDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动-奖品设置 BO
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityRewardSettingBO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 抽奖活动ID
     */
    private Long lotteryActivityId;

    /**
     * 等级：1-一等奖 2-二等奖 3-三等奖 (以此类推)
     */
    private Integer level;

    /**
     * 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会
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
     * 剩余数量
     */
    private Integer remainNumber;

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

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
