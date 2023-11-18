package com.yiling.hmc.settlement.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/4/7
 */
@Data
public class SettlementEnterprisePageBO implements Serializable {

    /**
     * 商家id
     */
    private Long eid;

    /**
     * 药品终端结算状态 1-待结算/2-已打款/3-无需结算失效单
     */
    private Integer terminalSettleStatus;

    /**
     * 订单数量
     */
    private Integer payCount;

    /**
     * 订单总额
     */
    private BigDecimal payAmount;
}
