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
 * @date: 2022/3/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ListCartRequest extends BaseRequest {

    /**
     * 买家EID
     */
    private Long buyerEid;

    /**
     * 配送商商品ID
     */
    private Long distributorEid;

    /**
     * 平台类型
     */
    private PlatformEnum platformEnum;

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
     * 创建人
     */
    private Long createUser;

}
