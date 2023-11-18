package com.yiling.admin.b2b.integral.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存积分抽奖配置 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveIntegralLotteryConfigForm extends BaseForm {

    /**
     * 消耗规则ID
     */
    @NotNull
    @ApiModelProperty(value = "消耗规则ID", required = true)
    private Long useRuleId;

    /**
     * 抽奖活动ID
     */
    @NotNull
    @ApiModelProperty(value = "抽奖活动ID", required = true)
    private Long lotteryActivityId;

    /**
     * 消耗积分值
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "消耗积分值", required = true)
    private Integer useIntegralValue;

    /**
     * 消耗总次数限制
     */
    @NotNull
    @ApiModelProperty(value = "消耗总次数限制", required = true)
    private Integer useSumTimes;

    /**
     * 单用户每天消耗总次数限制
     */
    @NotNull
    @ApiModelProperty(value = "单用户每天消耗总次数限制", required = true)
    private Integer everyDayTimes;

}
