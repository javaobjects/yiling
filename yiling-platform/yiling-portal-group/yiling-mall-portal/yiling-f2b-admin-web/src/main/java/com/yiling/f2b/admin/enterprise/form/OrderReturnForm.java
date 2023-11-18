package com.yiling.f2b.admin.enterprise.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
  * 申请退货请求参数
 * 
  *@author: tingwei.chen
  *@date: 2021/6/22
  *
  **/
@Data
@Accessors(chain = true)
@ApiModel
public class OrderReturnForm {

    /**
     * 交易单id
     */
    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "交易单号不能为空")
    private String                orderId;

    /**
     * 退货类型
     */
    @ApiModelProperty(value = "退货类型1-供应商退货单 2-破损退货单 3-采购退货单", required = true)
    private Integer               returnType;

    /**
     * 商品明细组装集合
     */
    @ApiModelProperty(value = "商品明细组装集合", required = true)
    @NotEmpty(message = "申请退货单明细不能为空")
    private List<OrderDetailForm> orderDetailList;

    @ApiModelProperty(value = "备注")
    private String                remark;
}
