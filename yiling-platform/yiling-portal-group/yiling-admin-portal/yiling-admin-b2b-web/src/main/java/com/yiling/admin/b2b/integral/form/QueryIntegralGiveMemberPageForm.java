package com.yiling.admin.b2b.integral.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单送积分-查询指定方案会员分页列表 Form
 *
 * @author: lun.yu
 * @date: 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralGiveMemberPageForm extends QueryPageListForm {

    /**
     * 发放规则ID
     */
    @NotNull
    @ApiModelProperty(value = "发放规则ID", required = true)
    private Long giveRuleId;
}
