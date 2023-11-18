package com.yiling.mall.cart.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/** 组合优惠添加
 * @author zhigang.guo
 * @date: 2022/4/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CombinationAddToCartRequest extends BaseRequest {


    /**
     * 组合优惠活动ID
     */
    @NotNull
    private Long promotionActivityId;

    /**
     * 添加数量
     */
    @NotNull
    private Integer quantity;

    /**
     * 配送商企业
     */
    @NotNull
    private Long distributorEid;

    /**
     * 买家企业ID
     */
    private Long buyerEid;

    /**
     * 平台类型
     */
    private PlatformEnum platformEnum;

    /**
     * 商品来源类型
     */
    private CartGoodsSourceEnum goodsSourceEnum;

}
