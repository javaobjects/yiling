package com.yiling.b2b.app.order.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * B2B移动端订单账期列表入参
 * @author:wei.wang
 * @date:2021/10/22
 */
@Data
public class OrderB2BPaymentFrom extends QueryPageListForm {
    /**
     * 订单单号或供应商名称
     */
    @ApiModelProperty(value = "订单单号或供应商名称", required = false)
    private String condition;

    /**
     * 还款状态 ：1-待还款 2-已还款
     */
    @ApiModelProperty(value = "还款状态：1-待还款 2-已还款", required = true)
    @NotNull(message = "不能为空")
    private Integer status;

    /**
     *授信主体
     */
    @ApiModelProperty(value = "授信主体", required = false)
    private Long sellerEid;

}
