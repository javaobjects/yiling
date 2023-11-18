package com.yiling.admin.sales.assistant.task.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 佣金政策 条件和佣金对
 * @author: ray
 * @date: 2021/9/29
 */
@Data
public class AddCommissionRuleForm {

    @ApiModelProperty(value = "条件")
    private String commissionCondition;
    @ApiModelProperty(value = "佣金：金额或者百分比")
    private String commission;
}