package com.yiling.hmc.insurance.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 保险关联商品
 *
 * @author: fan.shen
 * @date: 2022/5/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceGoodsDTO extends BaseDTO {

    /**
     * 保险id
     */
    private Long insuranceId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 售卖规格id
     */
    private Long sellSpecificationsId;

    /**
     * 商品市场价
     */
    private BigDecimal marketPrice;

    /**
     * 参保价
     */
    private BigDecimal insurancePrice;


}
