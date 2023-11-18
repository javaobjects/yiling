package com.yiling.admin.data.center.order.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 退货单返回给前端的数据实体
 *
 * @author: yong.zhang
 * @date: 2021/6/22
 **/
@Data
@Accessors(chain = true)
public class OrderReturnVO extends ReturnVO {

    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
    private Integer paymentMethod;

    @ApiModelProperty(value = "支付状态：1-待支付 2-已支付")
    private Integer paymentStatus;

    @ApiModelProperty(value = "应付金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty(value = "商品总额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "优惠总金额")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "发货数量")
    private Integer deliveryQuantity;

    @ApiModelProperty(value = "收货数量")
    private Integer receiveQuantity;

    @ApiModelProperty(value = "退货数量")
    private Integer returnQuantity;

    @ApiModelProperty(value = "退货商品件数")
    private Integer returnGoods;

    @ApiModelProperty(value = "退货商品总数量")
    private Integer returnGoodsNum;

    @ApiModelProperty(value = "收货人姓名")
    private String receiveUserName;

    @ApiModelProperty(value = "收货人手机号")
    private String receiveUserMobile;

    @ApiModelProperty(value = "收货人地址")
    private String receiveUserAdress;

    @ApiModelProperty(value = "驳回原因")
    private String refuseReason;

    @ApiModelProperty(value = "采购商备注-采购商下单的备注")
    private String orderRemark;

    @ApiModelProperty(value = "物流公司")
    private String deliveryCompany;

    @ApiModelProperty(value = "物流单号")
    private String deliveryNo;

    @ApiModelProperty(value = "商品明细组装集合")
    private List<OrderReturnGoodsDetailVO> orderDetailVOList;

    @ApiModelProperty(value = "退货单日志")
    private List<OrderLogVO> logList;

    @ApiModelProperty(value = "供应商企业信息")
    private EnterpriseInfoVO sellerEnterpriseInfo;

    @ApiModelProperty(value = "采购商企业信息")
    private EnterpriseInfoVO buyerEnterpriseInfo;

}
