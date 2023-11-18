package com.yiling.user.procrelation.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/7/2
 */
@Data
@Accessors(chain = true)
public class ProcRelationRebateResultBO implements Serializable {

    /**
     * 采购关系id
     */
    private Long relationId;

    /**
     * 版本id全局唯一，用于其他业务关联
     */
    private String versionId;

    /**
     * 商品Id
     */
    private Long goodsId;

    /**
     * 商品优惠折扣
     */
    private BigDecimal rebate;

    /**
     * 商品小计
     */
    private BigDecimal goodsAmount;

    /**
     * 优惠折扣金额
     */
    private BigDecimal rebateAmount;
}
