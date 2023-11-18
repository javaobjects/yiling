package com.yiling.b2b.app.order.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 退货单申请请求参数
 *
 * @author: yong.zhang
 * @date: 2021/10/20
 */
@Data
@Accessors(chain = true)
@ApiModel
public class B2BOrderReturnApplyForm {

    @ApiModelProperty(value = "交易订单号", required = true)
    @NotNull(message = "交易单号不能为空")
    private Long orderId;

    @ApiModelProperty(value = "订单号", required = true)
    private String orderNo;

//    @ApiModelProperty(value = "当前操作人", required = true)
//    private Long currentUserId;

    @ApiModelProperty(value = "申请退货单明细", required = true)
    @NotEmpty(message = "申请退货单明细不能为空")
    private List<B2BOrderReturnDetailApplyForm> orderReturnDetailList;

    @ApiModelProperty(value = "备注", required = true)
    private String remark;
}
