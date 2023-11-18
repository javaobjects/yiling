package com.yiling.admin.hmc.order.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 市场订单发货form
 * @author: benben.jia
 * @date:2023/03/06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrderDeliveryForm extends BaseForm {
    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID",required = true)
    @NotNull(message = "不能为空")
    private Long id;

    /**
     * 快递公司
     */
    @ApiModelProperty(value = "快递公司")
    private String deliveryCompany;

    /**
     * 快递单号
     */
    @ApiModelProperty(value = "快递单号")
    private String deliverNo;

}
