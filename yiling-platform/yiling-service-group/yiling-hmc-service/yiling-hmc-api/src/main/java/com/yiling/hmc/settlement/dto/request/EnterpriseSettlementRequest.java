package com.yiling.hmc.settlement.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/4/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseSettlementRequest extends BaseRequest {

    /**
     * 订单明细编号
     */
    private Long detailId;

    /**
     * 结账金额
     */
    private BigDecimal settleAmount;

    /**
     * 对账执行时间
     */
    private Date executionTime;

    /**
     * 结算完成时间
     */
    private Date settleTime;
}
