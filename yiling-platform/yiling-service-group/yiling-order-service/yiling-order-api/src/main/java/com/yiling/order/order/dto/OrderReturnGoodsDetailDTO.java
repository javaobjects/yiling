package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 *
 *@author:tingwei.chen
 *@date:2021/6/23
 */
@Data
public class OrderReturnGoodsDetailDTO {


    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 商品标准库ID
     */
    private Long standardId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品通用名
     */
    private String goodsCommonName;

    /**
     * 商品批准文号
     */
    private String goodsLicenseNo;

    /**
     * 商品规格
     */
    private String goodsSpecification;

    /**
     * 商品生产厂家
     */
    private String goodsManufacturer;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 购买数量
     */
    private Integer goodsQuantity;

    /**
     * 商品小计
     */
    private BigDecimal goodsAmount;

    /**
     * 发货数量
     */
    private Integer deliveryQuantity;

    /**
     * 收货数量
     */
    private Integer receiveQuantity;

    /**
     * 退货数量
     */
    private BigDecimal returnQuantity;
    /**
     * 商品对应批次集合
     */
    private List<OrderReturnGoodsBatchDTO> orderReturnGoodsBatchList;


}
