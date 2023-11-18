package com.yiling.f2b.admin.order.form;


import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 发货商品信息
 *
 * @author:wei.wang
 * @date:2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrderDeliveryInfoForm extends BaseForm {

    /**
     * 订单明细ID
     */
    @ApiModelProperty(value = "订单明细ID",required = true)
    @NotNull(message = "不能为空")
    private Long detailId;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "订单明细ID",required = true)
    @NotNull(message = "不能为空")
    private Long goodsId;

    /**
     * 发货内容
     */
    @ApiModelProperty(value = "发货内容",required = true)
    @NotEmpty(message = "不能为空")
    private List<@Valid DeliveryInfoForm> deliveryInfoList;

}
