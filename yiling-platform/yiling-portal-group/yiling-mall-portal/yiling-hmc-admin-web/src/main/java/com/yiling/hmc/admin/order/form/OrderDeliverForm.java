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
public class OrderDeliverForm extends BaseForm {

    @ApiModelProperty("订单id")
    @NotNull(message = "订单号不为为空")
    private Long orderId;

    @ApiModelProperty("快递公司")
    @NotBlank(message = "快递公司不能为空")
    private String deliverCompany;

    @ApiModelProperty("快递单号")
    @NotBlank(message = "快递单号不能为空")
    private String deliverNo;

    @ApiModelProperty("备注")
    private String remark;
}
