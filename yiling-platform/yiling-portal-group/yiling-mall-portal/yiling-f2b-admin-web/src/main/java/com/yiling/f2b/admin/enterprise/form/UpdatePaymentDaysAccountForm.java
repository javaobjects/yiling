package com.yiling.f2b.admin.enterprise.form;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdatePaymentDaysAccountForm extends BaseForm {

    @NotNull
    private Long id;

    /**
     * 账期额度
     */
    @NotNull
    @DecimalMin(value = "0.01", message = "额度不能小于0.01")
    @ApiModelProperty(value = "账期额度/（非以岭时）可用额度")
    private BigDecimal totalAmount;

    /**
     * 还款周期
     */
    @Range(min = 1)
    @NotNull
    @ApiModelProperty(value = "还款周期")
    private Integer period;

    /**
     * 是否长期有效：0-否 1-是
     */
    @NotNull
    @ApiModelProperty(value = "是否长期有效：0-否 1-是", required = true)
    private Integer longEffective;

    /**
     * 开始时间
     */
    @NotNull
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 状态：1-启用 2-停用
     */
    @Range(min = 1, max = 2)
    @NotNull
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer status;

}
