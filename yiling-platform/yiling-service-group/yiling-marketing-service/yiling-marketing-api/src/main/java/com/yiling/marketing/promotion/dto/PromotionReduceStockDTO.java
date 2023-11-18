package com.yiling.marketing.promotion.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 促销活动扣减库存
 *
 * @author: fan.shen
 * @date: 2022/1/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionReduceStockDTO extends BaseDTO {

    /**
     * 促销活动ID
     */
    private Long promotionActivityId;

    /**
     * 赠送商品ID
     */
    private Long goodsGiftId;

}
