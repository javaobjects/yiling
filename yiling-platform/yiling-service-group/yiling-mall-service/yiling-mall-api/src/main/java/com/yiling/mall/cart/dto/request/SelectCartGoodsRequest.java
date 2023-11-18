package com.yiling.mall.cart.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 勾选购物车商品 Request
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SelectCartGoodsRequest extends BaseRequest {

    /**
     * 购物车ID列表
     */
    private List<Long> ids;

    /**
     * 是否勾选
     */
    private Boolean selected;
}
