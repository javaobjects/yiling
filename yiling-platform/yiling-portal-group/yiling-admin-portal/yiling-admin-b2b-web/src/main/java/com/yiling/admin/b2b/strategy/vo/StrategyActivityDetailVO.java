package com.yiling.admin.b2b.strategy.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StrategyActivityDetailVO extends StrategyActivityVO {

    @ApiModelProperty("策略满赠客户数量")
    private Integer strategyBuyerLimitCount;

    @ApiModelProperty("策略满赠店铺SKU数量")
    private Integer strategyEnterpriseGoodsLimitCount;

    @ApiModelProperty("策略满赠会员方案数量")
    private Integer strategyMemberLimitCount;

    @ApiModelProperty("策略满赠推广方会员数量")
    private Integer strategyPromoterMemberLimitCount;

    @ApiModelProperty("策略满赠平台SKU数量")
    private Integer strategyPlatformGoodsLimitCount;

    @ApiModelProperty("策略满赠商家数量")
    private Integer strategySellerLimitCount;

    @ApiModelProperty("策略满赠购买会员数量")
    private Integer strategyStageMemberEffectCount;

    @ApiModelProperty("营销活动订单累计金额满赠内容阶梯")
    private List<StrategyAmountLadderVO> strategyAmountLadderList;

    @ApiModelProperty("营销活动时间周期满赠内容阶梯")
    private List<StrategyCycleLadderVO> strategyCycleLadderList;

    @ApiModelProperty("营销活动购买会员赠品信息")
    private List<StrategyGiftVO> strategyGiftList;

    // ===========================================================

    @ApiModelProperty("活动进度 0-全部 1-未开始 2-进行中 3-已结束")
    private Integer progress;

    @ApiModelProperty(value = "活动是否已开始：true-已开始 false-未开始")
    private Boolean running;
}
