package com.yiling.marketing.promotion.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionAppDTO extends BaseDTO {

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 满赠金额
     */
    private BigDecimal giftAmountLimit;

    /**
     * 赠品名称
     */
    private String giftName;
}
