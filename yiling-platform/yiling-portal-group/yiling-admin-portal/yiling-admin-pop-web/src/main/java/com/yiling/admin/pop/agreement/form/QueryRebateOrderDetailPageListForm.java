package com.yiling.admin.pop.agreement.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/8/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryRebateOrderDetailPageListForm extends BaseForm {

    /**
     * account
     */
    @NotEmpty
    @ApiModelProperty(value = "企业账号")
    private String  account;

    /**
     * 协议id
     */
    @NotNull
    @ApiModelProperty(value = "协议id")
    private Long    agreementId;

    /**
     * 1-可对账 2-不可对账
     */
    @ApiModelProperty(value = "1-可对账 2-不可对账，仅在查询待兑付订单明细时需要传该字段")
    private Integer conditionStatus;

    /**
     * 兑付状态 1-计算状态 2-已经兑付
     */
    @ApiModelProperty(value = "兑付状态 1-未兑付 2-已经兑付，，仅在查询以兑付订单明细时该字段传成2")
    private Integer cashStatus;

}
