package com.yiling.sales.assistant.task.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/14
 */
@Data
@Accessors(chain = true)
public class UpdateTaskGoodsRelationRequest implements Serializable {
    private static final long serialVersionUID = -7684710672747148327L;
    private Long taskGoodsId;

    private BigDecimal salePrice;

    private BigDecimal commission;

    private Long goodsId;

    private String commissionRate;

    private String goodsName;

    private BigDecimal outPrice;

    /**
     * 商销价
     */
    private BigDecimal  sellPrice;
}