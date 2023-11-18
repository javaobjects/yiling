package com.yiling.b2b.admin.order.form;

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
 * 订单发货form
 * @author:wei.wang
 * @date:2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrderDeliveryListInfoForm extends BaseForm {
    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID",required = true)
    @NotNull(message = "不能为空")
    private Long orderId;

    /**
     * 物流类型：1-自有物流 2-第三方物流
     */
    @ApiModelProperty(value = "物流类型：1-自有物流 2-第三方物流")
    private Integer deliveryType;

    /**
     * 物流公司
     */
    @ApiModelProperty(value = "物流公司")
    private String deliveryCompany;

    /**
     * 物流单号
     */
    @ApiModelProperty(value = "物流单号")
    private String deliveryNo;

    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息",required = true)
    @NotEmpty(message = "不能为空")
    private List<@Valid SaveOrderDeliveryInfoForm> orderDeliveryGoodsInfoList;
}
