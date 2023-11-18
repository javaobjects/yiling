package com.yiling.hmc.lotteryactivity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动-中奖信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-05
 */
@Data
@Accessors(chain = true)
public class LotteryActivityHitVO {

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String uname;

    /**
     * 奖品展示名称
     */
    @ApiModelProperty("奖品展示名称")
    private String showName;

    /**
     * 奖品名称
     */
    @ApiModelProperty("奖品名称")
    private String rewardName;


}
