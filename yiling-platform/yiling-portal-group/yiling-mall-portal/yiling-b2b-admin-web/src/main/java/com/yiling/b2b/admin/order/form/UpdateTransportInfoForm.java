package com.yiling.b2b.admin.order.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改物流信息
 * @author:wei.wang
 * @date:2023/5/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateTransportInfoForm  extends BaseForm {
    @ApiModelProperty(value = "物流类型：1-自有物流 2-第三方物流",required = true)
    @NotNull(message = "不能为空")
    private Integer deliveryType;

    @ApiModelProperty(value = "物流公司")
    private String deliveryCompany;

    @ApiModelProperty(value = "物流单号")
    private String deliveryNo;

    @ApiModelProperty(value = "订单单号", required = true)
    @NotNull(message = "订单单号不能为空")
    private Long orderId;
}
