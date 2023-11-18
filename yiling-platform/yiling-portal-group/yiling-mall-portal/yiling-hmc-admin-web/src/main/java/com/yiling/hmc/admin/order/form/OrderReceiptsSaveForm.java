package com.yiling.hmc.admin.order.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存订单票据请求参数
 *
 * @author: yong.zhang
 * @date: 2022/6/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReceiptsSaveForm extends BaseForm {

    @ApiModelProperty("订单id")
    @NotNull(message = "订单号不能为空")
    private Long orderId;

    @ApiModelProperty("订单票据")
    @NotEmpty(message = "订单票据不能为空")
    private List<String> orderReceiptsList;
}
