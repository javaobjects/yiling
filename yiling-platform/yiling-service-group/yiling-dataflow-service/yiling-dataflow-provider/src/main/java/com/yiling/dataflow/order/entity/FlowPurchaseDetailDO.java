package com.yiling.dataflow.order.entity;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/7/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FlowPurchaseDetailDO extends BaseDO {

    private Long eid;

    private String poDay;

    private BigDecimal poQuantity;

    private BigDecimal poMoney;
}
