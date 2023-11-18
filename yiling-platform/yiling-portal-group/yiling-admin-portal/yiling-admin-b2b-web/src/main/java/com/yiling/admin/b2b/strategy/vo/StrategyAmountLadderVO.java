package com.yiling.admin.b2b.strategy.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 营销活动订单累计金额满赠内容阶梯
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StrategyAmountLadderVO extends BaseVO {

    @ApiModelProperty("营销活动id")
    private Long marketingStrategyId;

    @ApiModelProperty("满赠金额条件")
    private BigDecimal amountLimit;

    @ApiModelProperty("策略满赠赠品表")
    private List<StrategyGiftVO> strategyGiftList;

}
