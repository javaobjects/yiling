package com.yiling.b2b.admin.order.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 退货单审核请求参数
 *
 * @author: yong.zhang
 * @date: 2021/10/20
 */
@Data
@Accessors(chain = true)
@ApiModel
public class B2BOrderReturnVerifyForm {

    @ApiModelProperty(value = "退货单id", required = true)
    @NotNull(message = "退货单不能为空")
    private Long returnId;

    @ApiModelProperty(value = "审核是否通过 0-通过 1-驳回", required = true)
    @NotNull(message = "审核状态不能为空")
    private Integer isSuccess;

    @ApiModelProperty(value = "驳回原因")
    private String failReason;
}
