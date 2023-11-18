package com.yiling.admin.b2b.integral.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单送积分-添加指定方案会员 Form
 *
 * @author: lun.yu
 * @date: 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddIntegralGiveMemberForm extends BaseForm {

    /**
     * 发放规则ID
     */
    @NotNull
    @ApiModelProperty(value = "发放规则ID", required = true)
    private Long giveRuleId;

    /**
     * 会员ID
     */
    @NotNull
    @ApiModelProperty(value = "会员ID", required = true)
    private Long memberId;

}
