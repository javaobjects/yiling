package com.yiling.f2b.admin.enterprise.form;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 集团账期总额度
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-02
 */
@Data
@Accessors(chain = true)
public class PaymentDaysCompanyForm extends BaseForm {

    private static final long serialVersionUID = 1L;

    /**
     * 账期总额度
     */
    @DecimalMin(value = "0.01",message = "额度不能小于0.01")
    @DecimalMax(value = "999999999999999999.99",message = "额度超过最大值")
    @ApiModelProperty("账期总额度")
    private BigDecimal totalAmount;

    /**
     * 已使用额度
     */
    @DecimalMin(value = "0.01",message = "额度不能小于0.01")
    @DecimalMax(value = "999999999999999999.99",message = "额度超过最大值")
    @ApiModelProperty("已使用额度")
    private BigDecimal usedAmount;

    /**
     * 已还款额度
     */
    @DecimalMin(value = "0.01",message = "额度不能小于0.01")
    @DecimalMax(value = "999999999999999999.99",message = "额度超过最大值")
    @ApiModelProperty("已还款额度")
    private BigDecimal repaymentAmount;


    /**
     * 集团可使用额度
     */
    @DecimalMin(value = "0.01",message = "额度不能小于0.01")
    @DecimalMax(value = "999999999999999999.99",message = "额度超过最大值")
    @ApiModelProperty("可使用额度")
    private BigDecimal availableAmount;

}
