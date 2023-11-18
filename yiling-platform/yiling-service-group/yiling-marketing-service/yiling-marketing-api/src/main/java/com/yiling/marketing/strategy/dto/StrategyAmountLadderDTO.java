package com.yiling.marketing.strategy.dto;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

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
public class StrategyAmountLadderDTO extends BaseDTO {

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 满赠金额条件
     */
    private BigDecimal amountLimit;

    /**
     * 赠品信息
     */
    private List<StrategyGiftDTO> strategyGiftList;
}
