package com.yiling.user.integral.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存积分抽奖配置 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveIntegralLotteryConfigRequest extends BaseRequest {

    /**
     * 消耗规则ID
     */
    @NotNull
    private Long useRuleId;

    /**
     * 抽奖活动ID
     */
    @NotNull
    private Long lotteryActivityId;

    /**
     * 消耗积分值
     */
    @NotNull
    private Integer useIntegralValue;

    /**
     * 消耗总次数限制
     */
    @NotNull
    private Integer useSumTimes;

    /**
     * 单用户每天消耗总次数限制
     */
    @NotNull
    private Integer everyDayTimes;

}
