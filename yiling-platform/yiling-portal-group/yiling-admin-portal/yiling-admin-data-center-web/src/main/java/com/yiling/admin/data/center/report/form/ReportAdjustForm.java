package com.yiling.admin.data.center.report.form;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReportAdjustForm extends BaseForm {

    /**
     * 报表id
     */
    @NotNull
    @ApiModelProperty("报表id")
    private Long reportId;

    /**
     * 调整金额
     */
    @NotNull
    @ApiModelProperty( "调整金额")
    private BigDecimal adjustAmount;

    /**
     * 调整原因
     */
    @ApiModelProperty( "调整原因")
    private String adjustReason;
}
