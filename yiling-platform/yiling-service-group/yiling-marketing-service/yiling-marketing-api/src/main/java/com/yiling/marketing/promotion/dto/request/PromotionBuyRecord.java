package com.yiling.marketing.promotion.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 促销活动购买记录
 *
 * @author fan.shen
 * @date 2022-2-8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionBuyRecord extends BaseRequest {

    /**
     * 订单表主键
     */
    private Long    orderId;

    /**
     * 购买数量
     */
    private Integer goodsQuantity;

    /**
     * 促销活动ID
     */
    private Long    promotionActivityId;

    /**
     * 企业ID-购买人id
     */
    private Long    eid;

    /**
     * 商品ID
     */
    private Long    goodsId;
}
