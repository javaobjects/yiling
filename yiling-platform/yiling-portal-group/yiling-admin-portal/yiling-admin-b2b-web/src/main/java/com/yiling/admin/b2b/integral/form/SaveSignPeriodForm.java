package com.yiling.admin.b2b.integral.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存签到周期 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveSignPeriodForm extends BaseForm {

    /**
     * 发放规则ID
     */
    @NotNull
    @ApiModelProperty(value = "发放规则ID", required = true)
    private Long giveRuleId;

    /**
     * 签到周期
     */
    @NotNull
    @Range(min = 5, max = 31)
    @ApiModelProperty(value = "签到周期", required = true)
    private Integer signPeriod;

    /**
     * 积分周期配置集合
     */
    @NotEmpty
    @ApiModelProperty(value = "积分周期配置集合", required = true)
    private List<@Valid SaveIntegralPeriodConfigForm> integralPeriodConfigList;




}
