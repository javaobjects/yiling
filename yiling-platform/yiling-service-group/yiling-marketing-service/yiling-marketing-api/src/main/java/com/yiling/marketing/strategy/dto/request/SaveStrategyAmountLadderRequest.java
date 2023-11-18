package com.yiling.marketing.strategy.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveStrategyAmountLadderRequest extends BaseRequest {

    /**
     * 阶梯id
     */
    private Long id;

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 满赠金额条件
     */
    private BigDecimal amountLimit;

    /**
     * 策略满赠赠品表
     */
    private List<SaveStrategyGiftRequest> strategyGiftList;
}
