package com.yiling.order.order.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/9/10
 */
@Data
public class OrderFlowBO implements java.io.Serializable {

    private Long detailId;

    private Long goodsId;

    private String goodsName;

    private String goodsInSn;

    private String sellerEname;

    private String buyerEname;

    private Long buyerChannelId;

    private String buyerChannelName;

    private String licenseNo;

    private String sellSpecifications;

    private String sellUnit;

    private String batchNo;

    private Integer goodsQuantity;

    private Integer deliveryQuantity;

    /**
     * 退回退货数量
     */
    private Integer returnQuantity;

    /**
     * 采购商退货数量
     */
    private Integer buyerReturnQuantity;

    private BigDecimal goodsPrice;

    private BigDecimal goodsAmount;

    private Date createTime;

    private Date deliveryTime;
}
