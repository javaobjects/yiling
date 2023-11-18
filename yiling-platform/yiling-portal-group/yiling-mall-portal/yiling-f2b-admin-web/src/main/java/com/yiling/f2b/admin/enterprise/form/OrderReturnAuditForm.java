package com.yiling.f2b.admin.enterprise.form;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
  *
  *@author:tingwei.chen
  *@date:2021/6/22
  *
  **/

@Data
@Accessors(chain = true)
@ApiModel
public class OrderReturnAuditForm {

    /**
     * 交易单id
     */
    @ApiModelProperty(value = "订单id", required = true)
    private Long orderId;

    /**
     * 退货单id
     */
    @ApiModelProperty(value = "退货单id", required = true)
    private Long orderReturnId;

    /**
     * 商品明细组装集合
     */
    @ApiModelProperty(value = "商品明细组装集合", required = true)
    private List<OrderDetailAuditForm> orderDetailList;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", required = true)
    private String remark;


}


