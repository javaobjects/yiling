package com.yiling.admin.b2b.strategy.form;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/8/30
 */
@Data
public class SaveStrategyCycleLadderForm implements Serializable {

    @ApiModelProperty("阶梯id")
    private Long id;

    @ApiModelProperty("营销活动id")
    private Long marketingStrategyId;

    @ApiModelProperty("阶梯(按周的话1-7逗号隔开，按月的话日期逗号隔开)")
    private List<Integer> conditionValue;

    @ApiModelProperty("执行次数")
    private Integer times;

    @ApiModelProperty("策略满赠赠品表")
    private List<SaveStrategyGiftForm> strategyGiftList;
}
