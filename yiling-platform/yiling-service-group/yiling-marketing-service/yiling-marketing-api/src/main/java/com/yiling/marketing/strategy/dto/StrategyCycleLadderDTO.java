package com.yiling.marketing.strategy.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 营销活动时间周期满赠内容阶梯
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StrategyCycleLadderDTO extends BaseDTO {

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 阶梯(按周的话1-7逗号隔开，按月的话日期逗号隔开)
     */
    private String conditionValue;

    /**
     * 执行次数
     */
    private Integer times;


}
