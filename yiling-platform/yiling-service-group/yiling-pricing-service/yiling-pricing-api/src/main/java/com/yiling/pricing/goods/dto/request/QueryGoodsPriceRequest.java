package com.yiling.pricing.goods.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询商品定价 request
 *
 * @author: yuecheng.chen
 * @date: 2021/6/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsPriceRequest extends BaseRequest {

    /**
     * 采购商ID
     */
    private Long customerEid;

    /**
     * 以岭商品ID列表
     */
    private List<Long> goodsIds;
}
