package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/** 预售订单表
 * @author zhigang.guo
 * @date: 2022/10/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PresaleOrderDTO extends OrderDTO {

    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /** {@Link com.yiling.order.order.enums.PreSaleActivityTypeEnum}
     * 预售活动类型:1-定金预售 2-全额预售
     */
    private Integer activityType;


    /**
     * 是否已支付定金
     */
    private Integer isPayDeposit;

    /**
     * 是否已支付尾款
     */
    private Integer isPayBalance;

    /**
     * 定金金额
     */
    private BigDecimal depositAmount;

    /**
     * 尾款金额
     */
    private BigDecimal balanceAmount;

    /**
     * 尾款支付开始时间
     */
    private Date balanceStartTime;

    /**
     * 尾款支付结束时间
     */
    private Date balanceEndTime;




}
