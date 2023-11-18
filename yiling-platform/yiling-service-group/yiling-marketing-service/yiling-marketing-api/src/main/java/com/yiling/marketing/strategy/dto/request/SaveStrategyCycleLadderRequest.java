package com.yiling.marketing.strategy.dto.request;

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
public class SaveStrategyCycleLadderRequest extends BaseRequest {

    /**
     * 阶梯id
     */
    private Long id;

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

    /**
     * 策略满赠赠品表
     */
    private List<SaveStrategyGiftRequest> strategyGiftList;
}
