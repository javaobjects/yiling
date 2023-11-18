package com.yiling.dataflow.order.dto;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/7/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FlowPurchaseDetailDTO extends BaseDTO {

    private Long eid;

    private String poDay;

    private BigDecimal poQuantity;

    private BigDecimal poMoney;
}
