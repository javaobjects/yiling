package com.yiling.hmc.admin.order.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReceivedForm extends BaseForm {

    @ApiModelProperty("订单id")
    @NotNull(message = "订单号不为为空")
    private Long orderId;

    @ApiModelProperty("保单id")
    @NotNull(message = "保单不能为空")
    private Long insuranceRecordId;

    @ApiModelProperty("保单人身份证后6位")
    private String cardLastSix;
}
