package com.yiling.f2b.admin.enterprise.form;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 创建账期账户 Form
 *
 * @author: xuan.zhou
 * @date: 2021/7/1
 */
@Data
public class CreatePaymentDaysAccountForm {

    /**
     * 采购商ID
     */
    @NotNull
    @ApiModelProperty(value = "采购商ID", required = true)
    private Long customerEid;

    /**
     * 供应商ID
     */
    @NotNull
    @ApiModelProperty(value = "供应商ID", required = true)
    private Long eid;


    /**
     * 账期额度
     */
    @NotNull
    @DecimalMin(value = "0.01", message = "额度不能小于0.01")
    @ApiModelProperty(value = "账期额度/（非以岭时）可用额度", required = true)
    private BigDecimal totalAmount;

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
    @ApiModelProperty(value = "开始时间", required = true)
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 还款周期
     */
    @Range(min = 1)
    @NotNull
    @ApiModelProperty(value = "还款周期", required = true)
    private Integer period;

    /**
     * 状态：1-启用 2-停用
     */
    @Range(min = 1, max = 2)
    @NotNull
    @ApiModelProperty(value = "状态：1-启用 2-停用", required = true)
    private Integer status;
}
