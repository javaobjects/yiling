package com.yiling.mall.cart.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 移除购物车商品
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemoveCartGoodsRequest extends BaseRequest {

    /**
     * 购物车ID列表
     */
    private List<Long> ids;
}
