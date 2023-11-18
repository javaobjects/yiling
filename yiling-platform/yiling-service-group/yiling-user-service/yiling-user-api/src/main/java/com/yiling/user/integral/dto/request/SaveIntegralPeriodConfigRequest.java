package com.yiling.user.integral.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存积分周期配置 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveIntegralPeriodConfigRequest extends BaseRequest {

    /**
     * 发放规则ID
     */
    private Long giveRuleId;

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
