package com.yiling.open.cms.diagnosis.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * IH 后台审方失败入参
 *
 * @author: fan.shen
 * @data: 2023/05/18
 */
@Data
@Accessors(chain = true)
public class HmcCancelPrescriptionOrderForm extends BaseForm {

    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "处方订单id")
    private Integer ihOrderId;

    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    @NotNull
    @ApiModelProperty(value = "退款记录id")
    private Long refundId;

    @NotNull
    @ApiModelProperty(value = "退款类型 1-全部退，2-部分退")
    private Integer refundType;

}
