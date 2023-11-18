package com.yiling.export.export.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/4/19
 */
@Data
public class HmcEnterpriseSettlementBO {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 售卖数量（真实给到用户货的退的不算）
     */
    private Long goodsQuantity;

    /**
     * 以岭给终端结算单价
     */
    private BigDecimal price;

    /**
     * 合计结算额
     */
    private BigDecimal goodsAmount;


    /**
     * 订单明细编号
     */
    private Long detailId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 创建日期
     */
    private String createTime;

    /**
     * 订单完成日期
     */
    private String finishTime;

    /**
     * 管控渠道
     */
    private String channelName;

    /**
     * 保司结算状态
     */
    private String insuranceSettlementStatus;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 药品服务终端名称
     */
    private String ename;

    /**
     * 药品服务终端ID
     */
    private String eid;
}
