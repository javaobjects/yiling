package com.yiling.hmc.settlement.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/4/6
 */
@Data
public class EnterpriseSettlementPageBO implements Serializable {

    /**
     * 订单明细id
     */
    private Long id;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 售卖规格id
     */
    private Long sellSpecificationsId;

    /**
     * 药品规格
     */
    private String goodsSpecifications;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 售卖数量
     */
    private Long goodsQuantity;

    /**
     * 结算单价
     */
    private BigDecimal price;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 合计
     */
    private BigDecimal goodsAmount;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 订单完成日期
     */
    private Date finishTime;

    /**
     * 保司结算状态:1-待结算/2-已结算/3-无需结算失效单/4-预付款抵扣已结
     */
    private Integer insuranceSettlementStatus;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 药品服务终端名称
     */
    private String ename;

    /**
     * 药品服务终端ID
     */
    private Long eid;

    // ============================================

    /**
     * 结账金额
     */
    private BigDecimal settlementAmount;

    /**
     * 对账执行时间
     */
    private Date executionTime;

    /**
     * 结算完成时间
     */
    private Date settlementTime;
}
