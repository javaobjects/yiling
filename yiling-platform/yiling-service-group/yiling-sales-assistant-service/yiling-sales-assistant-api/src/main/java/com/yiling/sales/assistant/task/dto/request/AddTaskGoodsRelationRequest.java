package com.yiling.sales.assistant.task.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddTaskGoodsRelationRequest extends BaseRequest {

    private static final long       serialVersionUID = -5716140687711375286L;
    private BigDecimal salePrice;

    private Long goodsId;
    private String goodsName;
    private BigDecimal commission;
    private String  commissionRate;

    private BigDecimal outPrice;

    /**
     * 商销价
     */
    private BigDecimal  sellPrice;
}