package com.yiling.admin.hmc.order.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/3/25
 */
@Data
public class OrderAndReturnDetailVO {

    @ApiModelProperty("订单信息")
    private OrderVO order;

    @ApiModelProperty("订单明细信息")
    private List<OrderDetailVO> orderDetailList;

    @ApiModelProperty("收货人信息")
    private OrderRelateUserVO orderRelateUser;

    @ApiModelProperty("退货单信息")
    private OrderReturnVO orderReturn;

    @ApiModelProperty("退货单明细信息")
    private List<OrderReturnDetailVO> orderReturnDetailList;

    @ApiModelProperty("保险服务提供商名称")
    private String insuranceCompanyName;

    @ApiModelProperty("下单用户名称")
    private String orderUserName;

    @ApiModelProperty("订单处理者id")
    private Long operateUserId;

    @ApiModelProperty("订单处理者名称")
    private String operateUserName;

    @ApiModelProperty("保险名称")
    private String insuranceName;

    @ApiModelProperty("投保人姓名")
    private String holderName;

    @ApiModelProperty("投保人联系方式")
    private String holderPhone;

    @ApiModelProperty("订单票据")
    private List<String> orderReceiptsList;

    @ApiModelProperty("处方详情")
    private OrderPrescriptionDetailVO orderPrescriptionDetail;
}
