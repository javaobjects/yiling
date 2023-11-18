package com.yiling.user.integral.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yiling.user.integral.dto.GenerateMultipleConfigDTO;
import com.yiling.user.integral.dto.IntegralPeriodConfigDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分消耗规则详情 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
@Data
@Accessors(chain = true)
public class IntegralUseRuleDetailBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 规则生效时间
     */
    private Date startTime;

    /**
     * 规则失效时间
     */
    private Date endTime;

    /**
     * 规则说明
     */
    private String description;

    /**
     * 行为ID
     */
    private Long behaviorId;

    /**
     * 行为名称
     */
    private String behaviorName;

    /**
     * 抽奖活动信息
     */
    private IntegralLotteryConfigBO integralLotteryConfig;



}
