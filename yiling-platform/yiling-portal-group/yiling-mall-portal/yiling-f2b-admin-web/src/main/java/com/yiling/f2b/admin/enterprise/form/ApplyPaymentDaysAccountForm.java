package com.yiling.f2b.admin.enterprise.form;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 账户临时额度 Form
 *
 * @author: tingwei.chen
 * @date: 2021/7/5
 */
@Data
@Accessors(chain = true)
public class ApplyPaymentDaysAccountForm {

    /**
     * 账户ID
     */
    @NotNull
    @ApiModelProperty(value = "采购商账户ID", required = true)
    private Long accountId;
    /**
     * 临时额度
     */
    @DecimalMin(value = "0.01",message = "额度不能小于0.01")
    @ApiModelProperty(value = "临时额度", required = true)
    private BigDecimal temporaryAmount;

    /**
     * 审核
     */
    @NotNull
    @ApiModelProperty(value = "审核", required = true)
    private int status;

}
