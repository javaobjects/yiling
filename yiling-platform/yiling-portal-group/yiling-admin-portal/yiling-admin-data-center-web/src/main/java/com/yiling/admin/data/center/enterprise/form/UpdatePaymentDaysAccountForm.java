package com.yiling.admin.data.center.enterprise.form;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

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

    private Long       id;

    /**
     * 账期额度
     */
    @NotNull
    @ApiModelProperty(value = "账期额度")
    private BigDecimal totalAmount;

    /**
     * 还款周期
     */
    @NotNull
    @ApiModelProperty(value = "还款周期")
    private Integer    period;

    /**
     * 开始时间
     */
    @NotNull
    @ApiModelProperty(value = "开始时间")
    private Date       startTime;

    /**
     * 结束时间
     */
    @NotNull
    @ApiModelProperty(value = "结束时间")
    private Date       endTime;

    /**
     * 状态：1-启用 2-停用
     */
    @NotNull
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer    status;

}
