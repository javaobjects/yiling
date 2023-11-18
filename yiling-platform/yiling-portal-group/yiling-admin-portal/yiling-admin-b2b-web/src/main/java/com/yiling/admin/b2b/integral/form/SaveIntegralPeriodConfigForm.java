package com.yiling.admin.b2b.integral.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存积分周期配置 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveIntegralPeriodConfigForm extends BaseForm {

    /**
     * 天数
     */
    @NotNull
    @Range(min = 1, max = 31)
    @ApiModelProperty(value = "天数", required = true)
    private Integer days;

    /**
     * 当天发放积分数
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "当天发放积分数", required = true)
    private Integer currentDayIntegral;

    /**
     * 连签奖励
     */
    @NotNull
    @ApiModelProperty(value = "连签奖励", required = true)
    private Integer continuousReward;


}
