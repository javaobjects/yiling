package com.yiling.admin.data.center.enterprise.form;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

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
     * 供应商ID
     */
    @NotNull
    @ApiModelProperty(value = "供应商ID", required = true)
    private Long       eid;

    /**
     * 采购商ID
     */
    @NotNull
    @ApiModelProperty(value = "采购商ID", required = true)
    private Long       customerEid;

    /**
     * 账期额度
     */
    @NotNull
    @ApiModelProperty(value = "账期额度", required = true)
    private BigDecimal totalAmount;

    /**
     * 开始时间
     */
    @NotNull
    @ApiModelProperty(value = "开始时间", required = true)
    private Date       startTime;

    /**
     * 结束时间
     */
    @NotNull
    @ApiModelProperty(value = "结束时间", required = true)
    private Date       endTime;

    /**
     * 还款周期
     */
    @NotNull
    @ApiModelProperty(value = "还款周期", required = true)
    private Integer    period;

    /**
     * 状态：1-启用 2-停用
     */
    @NotNull
    @ApiModelProperty(value = "状态：1-启用 2-停用", required = true)
    private Integer    status;
}
