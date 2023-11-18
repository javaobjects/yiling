package com.yiling.f2b.web.payment.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收营台页面 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/28
 */
@Data
public class ReceiptDeskPageVO {

    @ApiModelProperty("订单数量")
    private Integer orderNum;

    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("已选订单金额")
    private BigDecimal selectedAmount;

    @ApiModelProperty("折扣金额")
    private BigDecimal discountAmount;

    @ApiModelProperty("应付金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("订单列表")
    private List<OrderListItemVO> orderList;

    @Data
    public static class OrderListItemVO {

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

        @ApiModelProperty("支付类型列表")
        private List<PaymentTypeVO> paymentTypeList;

        @ApiModelProperty("是否可选")
        private Boolean selectEnabled;

        @ApiModelProperty("合同编号")
        private String contractNumber;

        @ApiModelProperty("是否选中")
        private Boolean selected;
    }

    @Data
    public static class PaymentTypeVO {

        @ApiModelProperty("支付类型ID")
        private Integer id;

        @ApiModelProperty("支付方式列表")
        private List<PaymentMethodVO> paymentMethodList;
    }

    @Data
    public static class PaymentMethodVO {

        @ApiModelProperty("支付方式ID")
        private Long id;

        @ApiModelProperty("支付方式名称")
        private String name;

        @ApiModelProperty("是否可用")
        private Boolean enabled;

        @ApiModelProperty("不可用原因")
        private String disabledReason;
    }
}
