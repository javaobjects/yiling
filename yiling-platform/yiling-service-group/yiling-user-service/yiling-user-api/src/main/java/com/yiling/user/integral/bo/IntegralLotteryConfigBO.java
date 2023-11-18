package com.yiling.user.integral.bo;

import java.io.Serializable;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分参与抽奖活动配置 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
@Data
@Accessors(chain = true)
public class IntegralLotteryConfigBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消耗规则ID
     */
    private Long useRuleId;

    /**
     * 抽奖活动ID
     */
    private Long lotteryActivityId;

    /**
     * 抽奖活动名称
     */
    private String lotteryActivityName;

    /**
     * 抽奖活动进度
     */
    private Integer lotteryActivityProgress;

    /**
     * 抽奖活动结束时间
     */
    private Date lotteryActivityEndTime;

    /**
     * 消耗积分值
     */
    private Integer useIntegralValue;

    /**
     * 消耗总次数限制
     */
    private Integer useSumTimes;

    /**
     * 单用户每天消耗总次数限制
     */
    private Integer everyDayTimes;

}
