package com.yiling.mall.cart.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;
import com.yiling.mall.cart.enums.CartIncludeEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.mall.cart.dto.request
 * @date: 2022/2/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetCartGoodsInfoRequest extends BaseRequest {

    /**
     * 商品SKUID
     */
    private Long            goodsSkuId;
    /**
     * 以岭商品ID
     */
    private Long            goodsId;
    /**
     * 买家EID
     */
    private Long            buyerEid;

    /**
     * 配送商商品ID
     */
    private Long            distributorEid;

    /**
     * 平台类型
     */
    private PlatformEnum    platformEnum;

    /**
     * 商品来源
     */
    private CartGoodsSourceEnum goodsSourceEnum;

    /**
     * 购物车是否包含
     */
    private CartIncludeEnum cartIncludeEnum;

    /**
     * 促销活动类型
     */
    private PromotionActivityTypeEnum promotionActivityTypeEnum;

    /**
     * 促销活动ID
     */
    private Long            promotionActivityId;
}
