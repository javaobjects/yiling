package com.yiling.sales.assistant.app.payment.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhigang.guo
 * @date: 2022/3/2
 */
@ApiModel("POP收银订单")
@Data
public class OrderListItemVO implements Serializable{

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("配送商企业ID")
    private Long distributorEid;

    @ApiModelProperty("配送商企业名称")
    private String distributorEname;

    @ApiModelProperty("订单金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("折扣金额")
    private BigDecimal discountAmount;

    @ApiModelProperty("应付金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("合同编号")
    private String contractNumber;

    @ApiModelProperty("支付类型列表")
    private List<PaymentTypeVO> paymentTypeList;

    @ApiModelProperty("是否可选")
    private Boolean selectEnabled;

    @ApiModelProperty("是否选中")
    private Boolean selected;

}
