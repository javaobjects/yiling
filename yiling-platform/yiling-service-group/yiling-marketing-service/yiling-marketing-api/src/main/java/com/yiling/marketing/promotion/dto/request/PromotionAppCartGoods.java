package com.yiling.marketing.promotion.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 购物车商品
 * @author: fan.shen
 * @date: 2022/1/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionAppCartGoods extends BaseRequest {

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品总额
     */
    private BigDecimal amount;
}
