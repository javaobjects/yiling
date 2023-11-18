package com.yiling.f2b.admin.enterprise.form;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新账期订单的还款金额 form
 * @author: lun.yu
 * @date: 2021/8/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdatePaymentOrderAmountForm extends BaseForm {

    /**
     * 应收单号
     */
    @NotEmpty
    @ApiModelProperty("应收单号")
    private String erpReceivableNo;

    /**
     * 还款金额
     */
    @NotNull
    @DecimalMin(value = "0.00",message = "还款金额不能小于0")
    @ApiModelProperty(value = "还款金额")
    private BigDecimal repaymentAmount;

}
