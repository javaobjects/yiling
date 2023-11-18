package com.yiling.order.order.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto.request
 * @date: 2021/10/27
 */
@Data
@Accessors(chain = true)
public class CreateOrderGiftRequest extends BaseRequest {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 赠品名称
     */
    private String giftName;

    /**
     * 商品赠品ID
     */
    private Long goodsGiftId;

    /**
     * 赠品价格
     */
    private BigDecimal price;

    /**
     * 促销活动ID
     */
    private Long promotionActivityId;

    /**
     * 价格区间ID
     */
    private Long promotionLimitId;

    /**
     * 描述
     */
    private String content;

}
