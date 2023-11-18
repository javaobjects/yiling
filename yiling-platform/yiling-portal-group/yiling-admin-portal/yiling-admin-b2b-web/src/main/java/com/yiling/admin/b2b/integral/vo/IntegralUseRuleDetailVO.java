package com.yiling.admin.b2b.integral.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分消耗规则详情 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class IntegralUseRuleDetailVO extends BaseVO {

    /**
     * 规则名称
     */
    @ApiModelProperty("规则名称")
    private String name;

    /**
     * 规则生效时间
     */
    @ApiModelProperty("规则生效时间")
    private Date startTime;

    /**
     * 规则失效时间
     */
    @ApiModelProperty("规则失效时间")
    private Date endTime;

    /**
     * 规则说明
     */
    @ApiModelProperty("规则说明")
    private String description;

    /**
     * 行为ID
     */
    @ApiModelProperty("行为ID")
    private Long behaviorId;

    /**
     * 行为名称
     */
    @ApiModelProperty("行为名称")
    private String behaviorName;

    /**
     * 抽奖活动信息配置
     */
    @ApiModelProperty("抽奖活动信息配置")
    private IntegralLotteryConfigVO integralLotteryConfig;

    @Data
    public static class IntegralLotteryConfigVO {

        @ApiModelProperty("消耗规则ID")
        private Long useRuleId;

        @ApiModelProperty("抽奖活动ID")
        private Long lotteryActivityId;

        @ApiModelProperty("抽奖活动名称")
        private String lotteryActivityName;

        @ApiModelProperty("抽奖活动状态：1-未开始 2-进行中 3-已结束")
        private Integer lotteryActivityProgress;

        @ApiModelProperty("抽奖活动结束时间")
        private Date lotteryActivityEndTime;

        @ApiModelProperty("消耗积分值")
        private Integer useIntegralValue;

        @ApiModelProperty("消耗总次数限制")
        private Integer useSumTimes;

        @ApiModelProperty("单用户每天消耗总次数限制")
        private Integer everyDayTimes;
    }

}
