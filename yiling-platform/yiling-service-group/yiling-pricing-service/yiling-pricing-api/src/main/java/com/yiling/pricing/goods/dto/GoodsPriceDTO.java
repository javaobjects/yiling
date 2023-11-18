package com.yiling.pricing.goods.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/2/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsPriceDTO  extends BaseDTO {

    private static final long serialVersionUID = 1L;


    /**
     * 购买价
     */
    private BigDecimal buyPrice;

    /**
     * 划线价
     */
    private BigDecimal linePrice;

    /**
     * 限价
     */
    private BigDecimal limitPrice;

    /**
     * 基价
     */
    private BigDecimal basePrice;

    /**
     * 是否展示
     */
    private Boolean isShow;
}
