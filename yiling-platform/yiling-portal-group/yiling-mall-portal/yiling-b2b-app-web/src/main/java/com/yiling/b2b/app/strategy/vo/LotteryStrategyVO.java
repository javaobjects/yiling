package com.yiling.b2b.app.strategy.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/10/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryStrategyVO extends BaseVO {

    /**
     * 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
     */
    @ApiModelProperty("策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员")
    private Integer strategyType;

    /**
     * 策略满赠内容
     */
    @ApiModelProperty("策略满赠内容")
    private String content;

    /**
     * 订单累计金额-限制订单状态(1-发货计算；2-下单计算)
     */
    @ApiModelProperty("订单累计金额-限制订单状态(1-发货计算；2-下单计算)")
    private Integer orderAmountStatusType;

    /**
     * 订单累计金额-阶梯匹配方式(1-按单累计匹配;2-活动结束整体匹配;3-按单匹配)
     */
    @ApiModelProperty("订单累计金额-阶梯匹配方式(1-按单累计匹配;2-活动结束整体匹配;3-按单匹配)")
    private Integer orderAmountLadderType;

    /**
     * 我的累计金额
     */
    @ApiModelProperty("我的累计金额")
    private BigDecimal amount;

    /**
     * 1-已完成;2-未满足
     */
    @ApiModelProperty("1-已完成;2-未满足")
    private Integer isSuccess;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("会员购买条件信息")
    private List<LotteryBuyStageMemberVO> lotteryBuyStageMemberList;

    //    @ApiModelProperty("会员购买条件id")
    //    private List<Long> buyStageMemberIdList;
}
