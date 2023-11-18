package com.yiling.mall.cart.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改购物车商品数量 Request
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCartGoodsQuantityRequest extends BaseRequest {

    /**
     * 购物车ID
     */
    private Long id;

    /**
     * 数量
     */
    private Integer quantity;
}
