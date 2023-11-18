package com.yiling.goods.medicine.dto.request;

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
public class QueryGoodsLimitPriceRequest extends BaseRequest {

    /**
     * 供应商ID
     */
    private Long eid;

    /**
     * 采购商ID
     */
    private Long customerEid;

    /**
     * 商品ID列表
     */
    private List<Long> goodsIds;

}
