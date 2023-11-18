package com.yiling.admin.b2b.strategy.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
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
public class StrategyCycleLadderVO extends BaseVO {

    @ApiModelProperty("营销活动id")
    private Long marketingStrategyId;

    @ApiModelProperty("阶梯(按周的话1-7逗号隔开，按月的话日期逗号隔开)")
    private List<Integer> conditionValue;

    @ApiModelProperty("执行次数")
    private Integer times;

    @ApiModelProperty("策略满赠赠品表")
    private List<StrategyGiftVO> strategyGiftList;
}
