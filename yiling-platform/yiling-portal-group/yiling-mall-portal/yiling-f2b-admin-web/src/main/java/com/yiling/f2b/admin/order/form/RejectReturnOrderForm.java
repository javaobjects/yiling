package com.yiling.f2b.admin.order.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/8/2
 */
@Data
@Accessors(chain = true)
@ApiModel
public class RejectReturnOrderForm implements java.io.Serializable {
    /**
     * 交易订单号
     */
    @ApiModelProperty(value = "交易订单号", required = true)
    private Long orderId;

    /**
     * 退货类型
     */
    @ApiModelProperty(value = "退货类型", required = true)
    private Integer returnType;

    /**
     * 退款订单编号
     */
    @NotNull(message = "退款订单编号不能为空")
    @ApiModelProperty(value = "退款订单编号", required = true)
    private Long returnId;

    /**
     * 当前操作人企业ID
     */
    @ApiModelProperty(value = "当前操作人企业ID", required = true)
    private Long currentEid;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @NotNull(message = "驳回原因不能为空")
    private String remark;
}
