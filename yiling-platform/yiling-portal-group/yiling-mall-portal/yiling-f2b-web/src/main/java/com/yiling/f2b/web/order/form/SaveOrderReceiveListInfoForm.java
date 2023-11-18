package com.yiling.f2b.web.order.form;

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
 * 订单收货from
 * @author:wei.wang
 * @date:2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrderReceiveListInfoForm extends BaseForm {
    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID",required = true)
    @NotNull(message = "不能为空")
    private Long orderId;

    /**
     * 回执单信息
     */
    @ApiModelProperty(value = "回执单信息")
    private List<String> receiveReceiptList;

    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息",required = true)
    @NotEmpty(message = "不能为空")
    private List< @Valid SaveOrderReceiveInfoForm> orderReceiveGoodsInfoList;
}
