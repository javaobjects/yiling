package com.yiling.admin.b2b.strategy.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/8/30
 */
@Data
public class SaveStrategyAmountLadderForm implements Serializable {
    
    @ApiModelProperty("阶梯id")
    private Long id;

    @ApiModelProperty("营销活动id")
    private Long marketingStrategyId;

    @ApiModelProperty("满赠金额条件")
    private BigDecimal amountLimit;

    @ApiModelProperty("策略满赠赠品表")
    private List<SaveStrategyGiftForm> strategyGiftList;
}
