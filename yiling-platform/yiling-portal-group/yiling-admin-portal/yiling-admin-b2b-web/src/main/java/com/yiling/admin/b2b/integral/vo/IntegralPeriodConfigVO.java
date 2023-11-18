package com.yiling.admin.b2b.integral.vo;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分周期配置 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralPeriodConfigVO extends BaseVO {

    /**
     * 天数
     */
    private Integer days;

    /**
     * 当天发放积分数
     */
    private Integer currentDayIntegral;

    /**
     * 连签奖励
     */
    private Integer continuousReward;

}
