package com.yiling.mall.cart.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;

import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加商品到购物车 Request
 *
 * @author: xuan.zhou
 * @date: 2021/6/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddToCartRequest extends BaseRequest {

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 配送商企业ID
     */
    private Long distributorEid;

    /**
     * 配送商商品ID
     */
    private Long distributorGoodsId;

    /**
     * 商品SkuId
     */
    private Long goodsSkuId;

    /**
     * 买家企业ID
     */
    private Long buyerEid;

    /**
     * 秒杀特价商品
     */
    private Integer promotionActivityType;

    /**
     * 促销活动ID
     */
    private Long promotionActivityId;

    /**
     * 平台类型
     */
    private PlatformEnum platformEnum;

    /**
     * 商品来源类型
     */
    private CartGoodsSourceEnum goodsSourceEnum;
}
